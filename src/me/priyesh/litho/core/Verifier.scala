/*
 * Copyright 2015 Priyesh Patel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.priyesh.litho
package core

import java.io.File

import com.google.typography.font.sfntly.Tag
import com.google.typography.font.sfntly.data.WritableFontData
import com.google.typography.font.sfntly.sfntly.testutils.TestFontUtils
import com.google.typography.font.sfntly.table.core.FontHeaderTable
import me.priyesh.litho.Strings._
import me.priyesh.litho.core.FontLoader._

object Verifier {

  def verify(folderName: String): CanFail = {
    val files = filesFromFolder(folderName)

    if (!FontLoader.folderExists(folderName)) {
      println(ErrorCantFindFolder)
      failed
    } else if (unrecognizedStyleFound(files)) {
      println(ErrorUnrecognizedStyle)
      failed
    } else {
      val filesAndStyles = filesAndStylesFromFolder(folderName)
      val invalidFonts = filesAndStyles filter { case (file, style) => !fontIsValid(file, style) }

      if (invalidFonts.nonEmpty) {
        println((if (invalidFonts.size == 1) "1 style is" else s"${invalidFonts.size} styles are") + " invalid:")
        invalidFonts.foreach(p => println(s"${p._2.name}"))
        failed
      } else {
        println(AllFontsValid)
        succeeded
      }
    }
  }

  def fix(folderName: String): CanFail = {
    val files = filesFromFolder(folderName)

    if (!FontLoader.folderExists(folderName)) {
      println(ErrorCantFindFolder)
      failed
    } else if (unrecognizedStyleFound(files)) {
      println(ErrorUnrecognizedStyle)
      failed
    } else {
      val filesAndStyles = filesAndStylesFromFolder(folderName)
      filesAndStyles.foreach(fixMacStyle _ tupled)
      println(FontFixingComplete)
      succeeded
    }
  }

  def fontIsValid(fontFile: File, style: FontStyle): Boolean = {
    val macStyle = FontLoader.fontFromFile(fontFile).getTable[FontHeaderTable](Tag.head).macStyleAsInt()
    val requiredFlags = getRequiredFlags(style)
    val allNeededFlags = requiredFlags.forall(flag => (macStyle & (1 << flag)) != 0)
    val noExtraFlags = Set(0, 1).diff(requiredFlags).forall(flag => (macStyle & (1 << flag)) == 0)
    allNeededFlags && noExtraFlags
  }

  private def fixMacStyle(file: File, style: FontStyle): File = {
    val fontBuilder = TestFontUtils.builderForFontFile(file)
    val headerTable = fontBuilder.getTableBuilder(Tag.head).build.asInstanceOf[FontHeaderTable]
    val writableFontData = WritableFontData.createWritableFontData(headerTable.readFontData())

    val headerTableBuilder = FontHeaderTable.Builder.createBuilder(headerTable.header(), writableFontData)
    headerTableBuilder.setMacStyleAsInt(getCorrectMacStyle(style))

    val fixedHeaderTable = headerTableBuilder.build()

    val fixedFontBuilder = TestFontUtils.builderForFontFile(file)
    fixedFontBuilder.newTableBuilder(Tag.head, fixedHeaderTable.readFontData())

    val fixedFont = fixedFontBuilder.build()
    TestFontUtils.serializeFont(fixedFont, file)
  }

  private def getRequiredFlags(style: FontStyle): Set[Int] = {
    import FontStyle._
    style match {
      case Bold | CondensedBold => Set(0)
      case Italic | CondensedItalic | LightItalic | ThinItalic => Set(1)
      case CondensedBoldItalic | BoldItalic => Set(0, 1)
      case CondensedRegular | Regular | Thin | Light => Set()
      case default => Set()
    }
  }

  private def getCorrectMacStyle(style: FontStyle): Int =
    getRequiredFlags(style).foldLeft(0) { (macStyleSoFar, newFlag) => macStyleSoFar | 1 << newFlag }
}