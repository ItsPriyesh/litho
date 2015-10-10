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

    Seq(
      test_packaging_from_basic_styles(),
      test_verifying_invalid_fonts(),
      test_packaging_invalid_fonts(),
      test_fixing_invalid_fonts()
    ) foreach run
  }

  def run(fun: TestFunction): Unit = {
    println(s"=== ${fun.name} ===")
    val result = fun.execute()
    if (fun.test.isDefined)  println(if (result.get) "Passed ✔" else "Failed ✖")
    println()
  }

  case class TestFunction(name: String,
                          private val before: () => Unit = () => (),
                          test: Option[() => Boolean] = None,
                          private val after: () => Unit = () => ()) {

    def execute(): Option[Boolean] = {
      before()
      val result = if (test.isDefined) Some(test.get.apply()) else None
      after()
      result
    }
  }

}