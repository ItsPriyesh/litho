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

  val AllStyles = Set(
    Regular, Italic,
    Bold, BoldItalic,
    Light, LightItalic,
    Thin, ThinItalic,
    CondensedRegular, CondensedItalic,
    CondensedBold, CondensedBoldItalic
  )

  val BasicStyles = Set(Regular, Italic, Bold, BoldItalic)

  // Map of styles to suitable replacement styles.
  // Replacements are ordered in descending order of closest style
  val StyleFallbackMap = Map(
    Light -> Seq(Thin, Regular),
    LightItalic -> Seq(ThinItalic, Italic),
    Thin -> Seq(Light, Regular),
    ThinItalic -> Seq(LightItalic, Italic),
    CondensedRegular -> Seq(Regular),
    CondensedItalic -> Seq(Italic),
    CondensedBold -> Seq(Bold),
    CondensedBoldItalic -> Seq(BoldItalic)
  )

  val FileNameToFontStyleMap = Map(
    "Regular.ttf" -> Regular,
    "Italic.ttf" -> Italic,
    "Bold.ttf" -> Bold,
    "BoldItalic.ttf" -> BoldItalic,
    "Light.ttf" -> Light,
    "LightItalic.ttf" -> LightItalic,
    "Thin.ttf" -> Thin,
    "ThinItalic.ttf" -> ThinItalic,
    "Condensed-Regular.ttf" -> CondensedRegular,
    "Condensed-Italic.ttf" -> CondensedItalic,
    "Condensed-Bold.ttf" -> CondensedBold,
    "Condensed-BoldItalic.ttf" -> CondensedBoldItalic
  )
  
}