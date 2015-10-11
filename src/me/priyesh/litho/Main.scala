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

import java.io.File

import me.priyesh.litho.Strings._
import me.priyesh.litho.core.FontLoader._
import me.priyesh.litho.core.{FontStyle, Verifier, Packager}
import me.priyesh.litho.core.Verifier._

object Main {

  def main(args: Array[String]): Unit = {
    def runIfFolderDefined(fun: (String) => Unit): Unit =
      if (args isDefinedAt 1) fun(args(1)) else println("A folder must be specified")

    if (args.isEmpty) showSplash()
    else if (args.length > 3) println("Too many arguments")
    else {
      args(0) match {
        case "verify" => runIfFolderDefined(verify)
        case "package" => runIfFolderDefined(buildPackage)
        case "fix" => runIfFolderDefined(fix)
        case default => println(s"'$default' is not a valid Litho command")
      }
    }
  }


  private def showSplash(): Unit = {
    import Config._
    println(SplashText)
    println(s"Version $Version || By $Author")
  }

  private def fix(folderName: String): Unit = {
    println("Fixing")

    val files = filesFromFolder(folderName)

    if (unrecognizedStyleFound(files)) {
      println(ErrorUnrecognizedStyle)
    } else {
      val filesAndStyles = filesAndStylesFromFolder(folderName)
      filesAndStyles.foreach((f: (File, FontStyle)) => {
        val dest = new File(f._1.getParentFile.getPath + "Generated/" + f._1.getName)
        Verifier.fixMacStyle(f._1, dest, f._2)
      })
    }
  }

  private def buildPackage(folderName: String): Unit = {
    println("Building package")
    Packager.buildPackage(folderName)
  }

  private def verify(folderName: String): Unit = {
    println("Verifying")

    val files = filesFromFolder(folderName)

    if (unrecognizedStyleFound(files)) {
      println(ErrorUnrecognizedStyle)
    } else {
      val filesAndStyles = filesAndStylesFromFolder(folderName)
      val invalidFonts = filesAndStyles filter { case (file, style) => !fontIsValid(file, style) }

      if (invalidFonts.nonEmpty) {
        println(s"${invalidFonts.size} styles are invalid:")
        invalidFonts.foreach(p => println(s"${p._2.name}"))
      } else {
        println("All fonts were valid")
      }
    }
  }
}