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

import me.priyesh.litho.Strings._
import me.priyesh.litho.core.{AndroidBridge, Packager, Verifier}

object Main {

  private val commandMapping: Map[String, (String) => CanFail] = Map(
    "verify" -> verify _,
    "fix" -> fix _,
    "package" -> buildPackage _,
    "install" -> install _,
    "all" -> doEverything _
  )

  def main(args: Array[String]): Unit = {
    def runIfFolderDefined(fun: (String) => CanFail): Unit =
      if (args isDefinedAt 1) fun(args(1)) else println(ErrorFolderUnspecified)

    if (args.isEmpty) showSplash()
    else if (args.length > 3) println(ErrorTooManyArgs)
    else commandMapping.get(args(0)).fold(println(s"${args(0)} is not a valid Litho command"))(runIfFolderDefined)
  }

  private def showSplash(): Unit = {
    import Config._
    println(SplashText)
    println(s"Version $Version || By $Author")
  }

  private def verify(folderName: String): CanFail = {
    println(Verifying)
    Verifier.verify(folderName)
  }

  private def fix(folderName: String): CanFail = {
    println(Fixing)
    Verifier.fix(folderName)
  }

  private def buildPackage(folderName: String): CanFail = {
    println(Packaging)
    Packager.buildPackage(folderName)
  }

  private def install(folderName: String): CanFail = {
    println(AndroidBridge.connectedDevices())
    print("Enter device id: ")
    val deviceId = readLine()
    println(Installing)
    AndroidBridge.install(deviceId, folderName)
  }

  private def doEverything(folderName: String): CanFail =
    verify(folderName) then buildPackage(folderName) then install(folderName + "FontPack")
}