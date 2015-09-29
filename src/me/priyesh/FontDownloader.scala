package me.priyesh

import java.io.File

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request.Builder
import okio.Okio
import rx.lang.scala.Observable

object FontDownloader {

  private val client: OkHttpClient = new OkHttpClient()

  def downloadFontPack(name: String): Observable[File] = downloadFiles(getUrls(name) zip getPaths(name))

  private def pathBuilder(name: String, style: FontStyle): String = s"./download/$name/${style.localName}"
  private def urlBuilder(name: String, style: FontStyle): String =
    s"https://raw.githubusercontent.com/ItsPriyesh/FontsterFontsRepo/master/${name}FontPack/${style.remoteName}"

  private def getUrls(fontName: String): List[String] = FontStyle.AllStyles.map(style => urlBuilder(fontName, style))
  private def getPaths(fontName: String): List[String] = FontStyle.AllStyles.map(style => pathBuilder(fontName, style))

  private def downloadFiles(urlsAndPaths: List[(String, String)]): Observable[File] = Observable
    .from(urlsAndPaths)
    .flatMap(downloadFile _ tupled)

  private def downloadFile(url: String, path: String): Observable[File] = Observable(subscriber => {
    println(s"Downloading $url")
    val request = new Builder().url(url).build()
    val file = new File(path)
    if (!file.exists()) {
      file.getParentFile.mkdirs()
      val response = client.newCall(request).execute()
        val sink = Okio.buffer(Okio.sink(file))
        sink.writeAll(response.body().source())
        sink.close()
    }

    subscriber.onNext(file)
    subscriber.onCompleted()
  })

}
