package me.priyesh

sealed case class FontStyle private(private val local: String, private val remoteMaybe: Option[String] = None) {
  def format(s: String): String = s"Roboto$s.ttf"

  val localName = format(local)
  val remoteName = remoteMaybe.getOrElse(localName)
}

object FontStyle {
  val Bold = FontStyle("-Bold")
  val BoldItalic = FontStyle("-BoldItalic")
  val Italic = FontStyle("-Italic")
  val Light = FontStyle("-Light")
  val LightItalic = FontStyle("-LightItalic")
  val Regular = FontStyle("-Regular")
  val Thin = FontStyle("-Thin")
  val ThinItalic = FontStyle("-ThinItalic")
  val CondensedBold = FontStyle("Condensed-Bold")
  val CondensedBoldItalic = FontStyle("Condensed-BoldItalic")
  val CondensedItalic = FontStyle("Condensed-Italic")
  val CondensedRegular = FontStyle("Condensed-Regular")
  val Black = FontStyle("-Black", Some(Bold.remoteName))
  val BlackItalic = FontStyle("-BlackItalic", Some(BoldItalic.remoteName))
  val Medium = FontStyle("-Medium", Some(Regular.remoteName))
  val MediumItalic = FontStyle("-MediumItalic", Some(Italic.remoteName))
  val CondensedLight = FontStyle("Condensed-Light", Some(CondensedRegular.remoteName))
  val CondensedLightItalic = FontStyle("Condensed-LightItalic", Some(CondensedItalic.remoteName))
  val AllStyles = List(Bold, BoldItalic, Italic, Light, LightItalic, Regular, Thin, ThinItalic, CondensedBold,
    CondensedBoldItalic, CondensedItalic, CondensedRegular, Black, BlackItalic, Medium, MediumItalic, CondensedLight,
    CondensedLightItalic)

  val BasicStyles = List(Regular, Italic, Bold, BoldItalic)

  val DerivativeMap = Map(
    Regular -> List(Thin, Light, CondensedRegular),
    Italic -> List(ThinItalic, LightItalic, CondensedItalic),
    Bold -> List(CondensedBold),
    BoldItalic -> List(CondensedBoldItalic)
  )
}