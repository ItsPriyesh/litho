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

package me.priyesh.litho

import me.priyesh.litho.core.FontLoader._
import me.priyesh.litho.core.FontStyle._
import me.priyesh.litho.core.Packager
import me.priyesh.litho.core.Verifier._

object Main {

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) showSplash()
    else {
      args(0) match {
        case "verify" => verify(args(1))
        case "package" => buildPackage(args(1))
        case "fix" => fix(args(1))
        case default => println(s"$default is not a valid argument")
      }
    }
  }

  def showSplash(): Unit = {
    import Config._
    println(SplashText)
    println(s"Version $Version || By $Author")
  }

  def fix(folderName: String): Unit = {
    println("Fixing")

    val files = filesFromFolder(folderName)

  }

  def buildPackage(folderName: String): Unit = {
    println("Building package")
    Packager.buildPackageFromBasics(folderName)
  }

  def verify(folderName: String): Unit = {
    println("Verifying")

    val files = filesFromFolder(folderName)

    if (unrecognizedStyleFound(files)) {
      println("An unrecognized style was found.\nEnsure that fonts in the specified folder are named correctly.")
    } else {
      val filesAndStyles = files.flatMap(file => FileNameToFontStyleMap.get(file.getName).map(style => (file, style))) toSet
      val validFonts = filesAndStyles filter { case (file, style) => fontIsValid(file, style) }
      val invalidFonts = filesAndStyles diff validFonts

      if (invalidFonts.nonEmpty) {
        println(s"${invalidFonts.size} styles are invalid:")
        invalidFonts.foreach(p => println(s"${p._2.name}"))
      } else {
        println("All fonts were valid")
      }
    }
  }
}