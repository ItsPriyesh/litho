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

import java.io.{File, FileInputStream}
import com.google.typography.font.sfntly.{Font, FontFactory}
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request.Builder
import me.priyesh.litho.core.FontStyle._
import okio.Okio

object FontLoader {

  private val client: OkHttpClient = new OkHttpClient()
  private val fontFactory = FontFactory.getInstance()

  def downloadFontPack(name: String): List[File] = downloadFiles(getUrls(name) zip getPaths(name))

  def loadFontPackFromDisk(name: String): List[File] = FontStyle.AllStyles.map(style => new File(urlBuilder(name, style)))

  def fontFromFile(file: File): Font = fontFactory.loadFonts(new FileInputStream(file))(0)

  def filesFromFolder(folderName: String): List[File] = {
    val folder = new File(folderName)
    def isVisibleFile(file: File): Boolean = file.isFile && !file.isHidden

    if (folder.exists && folder.isDirectory) folder.listFiles.filter(isVisibleFile).toList
    else List[File]()
  }

  def filesAndStylesFromFolder(folderName: String): Set[(File, FontStyle)] =
    filesFromFolder(folderName).flatMap(file => FileNameToFontStyleMap.get(file.getName).map(style => (file, style))) toSet

  def unrecognizedStyleFound(files: List[File]): Boolean = files.exists(file => FontStyle.FileNameToFontStyleMap.get(file.getName).isEmpty)

  private def pathBuilder(name: String, style: FontStyle): String = s"./download/$name/${style.name}"

  private def urlBuilder(name: String, style: FontStyle): String =
    s"https://raw.githubusercontent.com/ItsPriyesh/FontsterFontsRepo/master/${name}FontPack/${style.name}"

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