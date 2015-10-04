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

package me.priyesh

import java.io.File

import com.google.typography.font.sfntly.Tag
import com.google.typography.font.sfntly.table.core.FontHeaderTable

object Verifier {

  import FontStyle._

  def fontIsValid(fontFile: File, style: FontStyle): Boolean = {
    val macStyle = FontLoader.fontFromFile(fontFile).getTable[FontHeaderTable](Tag.head).macStyleAsInt()
    val requiredFlags = getRequiredFlags(style)
    val allNeededFlags = requiredFlags.forall(flag => (macStyle & (1 << flag)) != 0)
    val noExtraFlags = Set(0, 1).diff(requiredFlags).forall(flag => (macStyle & (1 << flag)) == 0)
    allNeededFlags && noExtraFlags
  }

  private def getRequiredFlags(style: FontStyle): Set[Int] = style match {
    case Black | Bold | CondensedBold => Set(0)
    case Italic | CondensedItalic | CondensedLightItalic | LightItalic | MediumItalic | ThinItalic => Set(1)
    case CondensedBoldItalic | BoldItalic | BlackItalic => Set(0, 1)
    case CondensedRegular | Regular | Thin | Medium | Light | CondensedLight => Set()
  }

  private def getCorrectMacStyle(style: FontStyle): Int =
    getRequiredFlags(style).foldLeft(0) { (macStyleSoFar, newFlag) => macStyleSoFar | 1 << newFlag }
}