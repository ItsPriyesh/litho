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

package me.priyesh.core

import java.io.File

import com.google.typography.font.sfntly.data.WritableFontData
import com.google.typography.font.sfntly.sfntly.testutils.TestFontUtils
import com.google.typography.font.sfntly.table.core.FontHeaderTable
import com.google.typography.font.sfntly.{FontFactory, Font, Tag}

object Verifier {

  def fontIsValid(fontFile: File, style: FontStyle): Boolean = {
    val macStyle = FontLoader.fontFromFile(fontFile).getTable[FontHeaderTable](Tag.head).macStyleAsInt()
    val requiredFlags = getRequiredFlags(style)
    val allNeededFlags = requiredFlags.forall(flag => (macStyle & (1 << flag)) != 0)
    val noExtraFlags = Set(0, 1).diff(requiredFlags).forall(flag => (macStyle & (1 << flag)) == 0)
    allNeededFlags && noExtraFlags
  }

  def fixMacStyle(sourceFile: File, destFile: File, style: FontStyle): File = {
    val originalFont = FontLoader.fontFromFile(sourceFile)
    val fontBuilder = TestFontUtils.builderForFontFile(sourceFile)
    val headerTable = fontBuilder.getTableBuilder(Tag.head).asInstanceOf[FontHeaderTable]
    val writableFontData = WritableFontData.createWritableFontData(headerTable.readFontData())

    val headerTableBuilder = FontHeaderTable.Builder.createBuilder(headerTable.header(), writableFontData)
    headerTableBuilder.setMacStyleAsInt(getCorrectMacStyle(style))

    val fixedHeaderTable = headerTableBuilder.build()
    
    val fixedFontBuilder = new Font.Builder(FontFactory.getInstance())
    fixedFontBuilder.setDigest(originalFont.digest())
    fixedFontBuilder.newTableBuilder(Tag.head, fixedHeaderTable.readFontData())

    val fixedFont = fixedFontBuilder.build()
    TestFontUtils.serializeFont(fixedFont,  destFile)
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