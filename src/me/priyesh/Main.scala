package me.priyesh

object Main {

  import Verifier._
  import FontLoader._

  def main(args: Array[String]) {
    val fontsAndStyles = downloadFontPack("Brixton").map(fontFromFile) zip FontStyle.AllStyles
    val validFonts = fontsAndStyles.filter(fontIsValid _ tupled)
    val invalidFonts = fontsAndStyles.filterNot(validFonts.contains)

    println(validFonts.size)
    println(invalidFonts.size)
  }
}
