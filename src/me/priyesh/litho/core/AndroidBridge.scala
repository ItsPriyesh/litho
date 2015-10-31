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

import java.io.OutputStreamWriter

import scala.sys.process._
object AndroidBridge {

  private def pushToSd(folderName: String): Boolean = {
    val pushCommand = s"adb push $folderName /sdcard/Litho/"
    pushCommand.! == 0
  }

  private def copyFontsToSystemAndReboot(): Boolean = {
    val io = BasicIO.standard { outStream =>
      val buffered = new OutputStreamWriter(outStream)
      buffered.write("su\n")
      buffered.write("cp /sdcard/Litho/*.ttf /system/fonts/\n")
      buffered.write("exit\n")
      buffered.close()
    }

    ("adb shell".run(io).exitValue() == 0) && ("adb reboot".! == 0)
  }

  def install(deviceId: String, folderName: String): Unit = {
    if (pushToSd(folderName)) copyFontsToSystemAndReboot()
  }

}
