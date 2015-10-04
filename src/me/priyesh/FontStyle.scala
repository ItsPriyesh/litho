package me.priyesh

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
    Regular -> List(Thin, Light, CondensedRegular),
    Italic -> List(ThinItalic, LightItalic, CondensedItalic),
    Bold -> List(CondensedBold),
    BoldItalic -> List(CondensedBoldItalic)
  )
}