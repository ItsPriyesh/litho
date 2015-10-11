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
import me.priyesh.litho.core.FontStyle._

object FontLoader {

  private val fontFactory = FontFactory.getInstance()

  def folderExists(folderName: String) = new File(s"./$folderName").exists()

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

}