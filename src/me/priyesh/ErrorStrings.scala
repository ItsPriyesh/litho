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

object ErrorStrings {
  val InvalidFiles = "Folder contains invalid fonts. Only .ttf files are allowed."
  val InvalidFileCount = "Folder contains invalid number of fonts."
  val BasicsMissing = "Litho was unable to locate all basic files."
  val EnsureBasicsExist = "Ensure that you include the following styles: Regular, Italic, Bold, BoldItalic."
  val InvalidMacStyles = "Some fonts were found to have incorrect macstyles assigned"
}
