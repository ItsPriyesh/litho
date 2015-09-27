package me.priyesh

object Main {
  def main(args: Array[String]) {
    FontDownloader.downloadFontPack("Aleo").subscribe()
  }
}
