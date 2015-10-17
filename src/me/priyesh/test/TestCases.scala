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
import java.nio.file.{Files, StandardCopyOption}

import me.priyesh.litho.Main
import me.priyesh.litho.core.{FontLoader, FontStyle, Verifier}
import me.priyesh.test.TestRunner.TestFunction

object TestCases {

  /* These fonts have correct macstyles */
  private val ValidFonts = "testResources/Aleo"

  /* One of these fonts has an incorrect macstyle (Roboto-Italic.ttf) */
  private val InvalidFonts = "testResources/Aleo_borked"

  private def deleteFolder(name: String): Unit = {
    val folder = new File(name)
    if (folder.exists) {
      folder.listFiles().map(_.toPath).foreach(Files.delete)
      Files.delete(folder.toPath)
    }
  }

  private def copyFolder(original: String, copy: String): Unit = {
    val copyFolder = new File(copy)
    copyFolder.mkdirs()
    FontLoader.filesFromFolder(original).foreach(original => {
      val copy = new File(copyFolder.getPath + "/" + original.getName)
      Files.copy(original.toPath, copy.toPath, StandardCopyOption.REPLACE_EXISTING)
    })
  }

  def test_packaging_from_basic_styles() = {
    val folderName = ValidFonts
    TestFunction("Testing packaging from basic styles",
      test = () => {
        val args = Array("package", folderName)
        Main.main(args)
        FontStyle.AllStyles.forall(style => new File(s"${folderName}FontPack/${style.name}").exists())
      },
      after = () => {
        deleteFolder(folderName + "FontPack")
      }
    )
  }

  def test_verifying_invalid_fonts() = {
    val folderName = InvalidFonts
    TestFunction("Testing attempt to verify fonts with invalid macStyles",
      test = () => {
        val args = Array("verify", folderName)
        Main.main(args)

        FontLoader.filesAndStylesFromFolder(folderName).exists(f => !Verifier.fontIsValid(f._1, f._2))
      }
    )
  }

  def test_packaging_invalid_fonts() = {
    val folderName = InvalidFonts
    TestFunction("Testing attempt to package fonts with invalid macStyles",
      test = () => {
        val args = Array("package", folderName)
        Main.main(args)

        FontLoader.filesAndStylesFromFolder(folderName).exists(f => !Verifier.fontIsValid(f._1, f._2))
      },
      after = () => {
        deleteFolder(folderName + "FontPack")
      }
    )
  }

  def test_fixing_invalid_fonts() = {
    import Verifier._
    val folderName = InvalidFonts
    val tempFolderName = InvalidFonts + "_temp"
    copyFolder(folderName, tempFolderName)
    TestFunction("Testing fixing fonts with invalid macStyles",
      test = () => {
        val args1 = Array("verify", tempFolderName)
        Main.main(args1)

        val args2 = Array("fix", tempFolderName)
        Main.main(args2)

        FontLoader.filesAndStylesFromFolder(tempFolderName).forall(fontIsValid _ tupled)
      },
      after = () => {
        deleteFolder(tempFolderName)
      }
    )
  }
}
