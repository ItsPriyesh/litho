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

import me.priyesh.litho.core.{Packager, Verifier}
import Strings._

object Main {

  private object Commands {
    val Verify = "verify"
    val Fix = "fix"
    val Package = "package"
  }

  def main(args: Array[String]): Unit = {
    def runIfFolderDefined(fun: (String) => Unit): Unit =
      if (args isDefinedAt 1) fun(args(1)) else println(ErrorFolderUnspecified)

    if (args.isEmpty) showSplash()
    else if (args.length > 3) println(ErrorTooManyArgs)
    else {
      import Commands._
      args(0) match {
        case Verify => runIfFolderDefined(verify)
        case Fix => runIfFolderDefined(fix)
        case Package => runIfFolderDefined(buildPackage)
        case default => println(s"'$default' is not a valid Litho command")
      }
    }
  }

  private def showSplash(): Unit = {
    import Config._
    println(SplashText)
    println(s"Version $Version || By $Author")
  }

  private def verify(folderName: String): Unit = {
    println(Verifying)
    Verifier.verify(folderName)
  }

  private def fix(folderName: String): Unit = {
    println(Fixing)
    Verifier.fix(folderName)
  }

  private def buildPackage(folderName: String): Unit = {
    println(Packaging)
    Packager.buildPackage(folderName)
  }

}