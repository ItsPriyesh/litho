package me.priyesh

import java.io.{FileInputStream, File}

import com.google.typography.font.sfntly.{FontFactory, Font}
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request.Builder
import okio.Okio

object FontLoader {

  private val client: OkHttpClient = new OkHttpClient()
  private val fontFactory = FontFactory.getInstance()

  def downloadFontPack(name: String): List[File] = downloadFiles(getUrls(name) zip getPaths(name))

  def loadFontPackFromDisk(name: String): List[File] = FontStyle.AllStyles.map(style => new File(urlBuilder(name, style)))

  def fontFromFile(file: File): Font = fontFactory.loadFonts(new FileInputStream(file))(0)

  private def pathBuilder(name: String, style: FontStyle): String = s"./download/$name/${style.localName}"

  private def urlBuilder(name: String, style: FontStyle): String =
    s"https://raw.githubusercontent.com/ItsPriyesh/FontsterFontsRepo/master/${name}FontPack/${style.remoteName}"

  private def getUrls(fontName: String): List[String] = FontStyle.AllStyles.map(style => urlBuilder(fontName, style))

  private def getPaths(fontName: String): List[String] = FontStyle.AllStyles.map(style => pathBuilder(fontName, style))

  private def downloadFiles(urlsAndPaths: List[(String, String)]): List[File] = urlsAndPaths.map(downloadFile _ tupled)

  private def downloadFile(url: String, path: String): File = {
    val request = new Builder().url(url).build()
    val file = new File(path)
    if (!file.exists()) {
      file.getParentFile.mkdirs()
      val response = client.newCall(request).execute()
      val sink = Okio.buffer(Okio.sink(file))
      sink.writeAll(response.body().source())
      sink.close()
    }
    file
  }

}