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
package core

import java.io.{OutputStream, OutputStreamWriter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise
import scala.sys.process._

object AndroidBridge {

  private def pushToSd(source: String): CanFail = if (s"adb push $source /sdcard/Litho/".! == 0) Succeeded else Failed

  private def copyFontsToSystemAndReboot(deviceId: String): Unit = {
    val inputStreamPromise = Promise[OutputStream]()
    def writeCommands(inputStream: OutputStream): Unit = {
      val buffered = new OutputStreamWriter(inputStream)
      buffered.write("su\n")
      buffered.write("mount -o rw,remount /system\n")
      buffered.write("cp /sdcard/Litho/*.ttf /system/fonts/\n")
      buffered.write("exit\n")
      buffered.write("exit\n")
      buffered.close()
    }

    s"adb shell -s $deviceId".run(new ProcessIO({ inputStream =>
      inputStreamPromise.success(inputStream)
    }, { outputStream =>
      print(outputStream.read())
      inputStreamPromise.future.foreach(writeCommands)
      BasicIO.toStdOut(outputStream)
    }, { _ => }))
    println("Process finished")
    (5 to 1 by -1).foreach(n => {
      Thread.sleep(1000)
      println(s"Rebooting in $n seconds")
    })
    "adb reboot".!
  }

  def connectedDevices(): String = "adb devices".!!

  def install(deviceId: String, folderName: String): CanFail = {
    pushToSd("./RobotoFlashable.zip") then pushToSd(folderName) foreach {
      copyFontsToSystemAndReboot(deviceId)
      println("Installation complete!")
    }
  }

}
