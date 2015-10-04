package me.priyesh

object Main {

  import FontLoader._
  import Verifier._

  def main(args: Array[String]): Unit = {
    val fontsAndStyles = downloadFontPack("Brixton").map(fontFromFile) zip FontStyle.AllStyles toSet
    val validFonts = fontsAndStyles filter (fontIsValid _ tupled)
    val invalidFonts = fontsAndStyles diff validFonts

    if (invalidFonts.nonEmpty) {
      println(s"${invalidFonts.size} styles are invalid:")
      invalidFonts.foreach(p => println(s"${p._2.localName}"))
    } else {
      println("All fonts were valid")
    }
  }
}