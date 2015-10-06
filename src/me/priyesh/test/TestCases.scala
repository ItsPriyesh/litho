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

package me.priyesh.test

import java.io.File
import java.nio.file.Files

import me.priyesh.litho.Main
import me.priyesh.litho.core.FontStyle
import me.priyesh.test.TestRunner.TestFunction

object TestCases {

  def testPackagingFromBasicStyles() = {
    val folderName = "test/Aleo"
    TestFunction("testPackagingFromBasicStyles",
      test = () => {
        val args = Array("package", folderName)
        Main.main(args)
        def fileExists(style: FontStyle): Boolean = new File(s"${folderName}Generated/${style.name}").exists()
        FontStyle.AllStyles.forall(fileExists)
      },
      after = () => {
        new File(s"${folderName}Generated/").listFiles().map(_.toPath).foreach(Files.delete)
        Files.delete(new File(s"${folderName}Generated/").toPath)
      })
  }
  
}
