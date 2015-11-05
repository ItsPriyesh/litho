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

import scala.util.Try

sealed trait CanFail {
  def flatMap[A](fun: () => CanFail): CanFail = {
    this match {
      case Succeeded => fun()
      case Failed => Failed
    }
  }
  def map[A](fun: () => Unit): CanFail = {
    this match {
      case Succeeded => fun(); Succeeded
      case Failed => Failed
    }
  }
  def foreach[A](fun: => A): CanFail = {
    this match {
      case Succeeded => fun; Succeeded
      case Failed => Failed
    }
  }
  def fold[A](succeeded: => A)(failed: => A): A = this match {
    case Succeeded => succeeded
    case Failed => failed
  }
  def and(other: CanFail): CanFail = {
    flatMap(() => other)
  }
  def then(other: => CanFail): CanFail = {
    flatMap(() => other)
  }
}
case object Succeeded extends CanFail
case object Failed extends CanFail

object CanFail {
  def apply[T](f: => T): CanFail = Try(f).toOption.fold(failed)(_ => succeeded)
}