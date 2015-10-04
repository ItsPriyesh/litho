package me.priyesh

import me.priyesh.FontLoader._
import me.priyesh.Verifier._

object Main {

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) showSplash()
    args(0) match {
      case "verify" => verify(args(1))
      case "package" => buildPackage(args(1))
      case default => println(s"$default is not a valid argument")
    }
  }

  def showSplash(): Unit = {

  }

  def buildPackage(folderName: String): Unit = {
    val files = filesFromFolder(folderName)
    Packager.buildPackageFromBasics(files)
  }

  def verify(folderName: String): Unit = {
    val filesAndFontStyles = (filesFromFolder(folderName) zip FontStyle.AllStyles) toSet
    val validFonts = filesAndFontStyles filter { case (file, style) => fontIsValid(file, style) }
    val invalidFonts = filesAndFontStyles diff validFonts

    if (invalidFonts.nonEmpty) {
      println(s"${invalidFonts.size} styles are invalid:")
      invalidFonts.foreach(p => println(s"${p._2.localName}"))
    } else {
      println("All fonts were valid")
    }
  }
}