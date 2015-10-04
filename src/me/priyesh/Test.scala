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

import java.io.File

object Test {

  def main(args: Array[String]): Unit = {
    displayResult(testPackager)
  }
  
  def displayResult(result: Boolean): Unit = println(s"Packager test ${if (result) "passed" else "failed"}")

  def testPackager: Boolean = {
    val folderName = "Aleo"
    val args = Array("package", folderName)

    Main.main(args)

    def fileExists(style: FontStyle): Boolean = new File(s"./${folderName}Generated/${style.name}").exists()
    FontStyle.AllStyles.forall(fileExists)
  }
}
