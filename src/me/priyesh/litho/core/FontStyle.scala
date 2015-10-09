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

package me.priyesh.litho.core

sealed case class FontStyle private(private val s: String) {
  val name = s"Roboto$s.ttf"
}

object FontStyle {
  val Regular = FontStyle("-Regular")
  val Italic = FontStyle("-Italic")
  val Bold = FontStyle("-Bold")
  val BoldItalic = FontStyle("-BoldItalic")
  val Light = FontStyle("-Light")
  val LightItalic = FontStyle("-LightItalic")
  val Thin = FontStyle("-Thin")
  val ThinItalic = FontStyle("-ThinItalic")
  val CondensedRegular = FontStyle("Condensed-Regular")
  val CondensedItalic = FontStyle("Condensed-Italic")
  val CondensedBold = FontStyle("Condensed-Bold")
  val CondensedBoldItalic = FontStyle("Condensed-BoldItalic")

  val AllStyles = List(
    Regular, Italic,
    Bold, BoldItalic,
    Light, LightItalic,
    Thin, ThinItalic,
    CondensedRegular, CondensedItalic,
    CondensedBold, CondensedBoldItalic
  )

  val BasicStyles = List(Regular, Italic, Bold, BoldItalic)

  val DerivativeMap = Map(
    Regular -> List(Regular, Thin, Light, CondensedRegular),
    Italic -> List(Italic, ThinItalic, LightItalic, CondensedItalic),
    Bold -> List(Bold, CondensedBold),
    BoldItalic -> List(BoldItalic, CondensedBoldItalic)
  )

  val FileNameToFontStyleMap = Map(
    "Roboto-Regular.ttf" -> Regular,
    "Roboto-Italic.ttf" -> Italic,
    "Roboto-Bold.ttf" -> Bold,
    "Roboto-BoldItalic.ttf" -> BoldItalic,
    "Roboto-Light.ttf" -> Light,
    "Roboto-LightItalic.ttf" -> LightItalic,
    "Roboto-Thin.ttf" -> Thin,
    "Roboto-ThinItalic.ttf" -> ThinItalic,
    "RobotoCondensed-Regular.ttf" -> CondensedRegular,
    "RobotoCondensed-Italic.ttf" -> CondensedItalic,
    "RobotoCondensed-Bold.ttf" -> CondensedBold,
    "RobotoCondensed-BoldItalic.ttf" -> CondensedBoldItalic
  )

}