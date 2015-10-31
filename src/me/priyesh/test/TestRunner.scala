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

object TestRunner {

  def main(args: Array[String]): Unit = {
    import TestCases._

    val testCases = Seq(
      test_packaging_from_basic_styles(),
      test_verifying_invalid_fonts(),
      test_packaging_invalid_fonts(),
      test_fixing_invalid_fonts(),
      test_install()
    ) map run

    val passedTests = testCases count identity
    val totalTests = testCases size

    println("===== TEST SUMMARY =====")
    println(s"Passed: $passedTests / $totalTests (${(passedTests.toDouble / totalTests.toDouble) * 100}%)")
    println(if (passedTests == totalTests) "All tests passed" else "Some tests failed")
  }

  private def run(fun: TestFunction): Boolean = {
    println(s"=== ${fun.name} ===")
    val result = fun.test()
    println((if (result) "Passed ✔" else "Failed ✖") + "\n")
    fun.after()
    result
  }

  final case class TestFunction(name: String, test: () => Boolean, after: () => Unit = () => ())

}