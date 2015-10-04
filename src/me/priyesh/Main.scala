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
      invalidFonts.foreach(p => println(s"${p._2.name}"))
    } else {
      println("All fonts were valid")
    }
  }
}