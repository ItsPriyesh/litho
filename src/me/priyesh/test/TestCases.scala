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

  /* These fonts have correct macstyles */
  private val ValidFonts = "testResources/Aleo"

  /* One of these fonts has an incorrect macstyle (Roboto-Italic.ttf) */
  private val InvalidFonts = "testResources/Aleo_borked"

  private def deleteGeneratedFolder(name: String): Unit = {
    val folder = new File(s"${name}Generated/")
    if (folder.exists) {
      folder.listFiles().map(_.toPath).foreach(Files.delete)
      Files.delete(folder.toPath)
    }
  }

  def test_packaging_from_basic_styles() = {
    val folderName = ValidFonts
    TestFunction("Testing Packaging from Basic Styles",
      test = Some(() => {
        val args = Array("package", folderName)
        Main.main(args)
        def fileExists(style: FontStyle): Boolean = new File(s"${folderName}Generated/${style.name}").exists()
        FontStyle.AllStyles.forall(fileExists)
      }),
      after = () => {
        deleteGeneratedFolder(folderName)
      })
  }

  def test_verifying_invalid_fonts() = {
    val folderName = InvalidFonts
    TestFunction("Testing attempt to verify fonts with invalid macstyles",
      before = () => {
        val args = Array("verify", folderName)
        Main.main(args)
      })
  }

  def test_packaging_fail_fix_re_verify_succeed() = {
    val folderName = InvalidFonts
    TestFunction("Testing packaging (and failing), fixing, re-verifying (and succeeding)",
      before = () => {
        val args = Array("package", folderName)
        Main.main(args)
      },
      after = () => {
        deleteGeneratedFolder(folderName)
      })
  }
}
