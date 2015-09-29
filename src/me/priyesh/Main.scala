package me.priyesh

import java.io.{FileInputStream, File}
import com.google.typography.font.sfntly.{FontFactory, Font}

object Main {
  def main(args: Array[String]) {
    FontDownloader.downloadFontPack("Aleo").zip(FontStyle.AllStyles).map {
      case (fontFile, style) =>
        val font = loadFont(fontFile)
        Verifier.verifyFont(style, font).fold {
          println("Font couldn't be verified!")
        } { _ =>
          println("Font valid!")
        }
    }.subscribe()
  }

  private val fontFactory = FontFactory.getInstance()

  private def loadFont(file: File): Font = fontFactory.loadFonts(new FileInputStream(file))(0)
}
