Litho
=====
A tool for creating, verifying and fixing font packages used by [Fontster][1].

Fontster works by replacing the [twelve font styles][2] used by the system. In the context of the app, these twelve styles can be referred to as a font pack. Building font packs isn't always as simple as gathering and renaming TrueType fonts. Each style must have the correct `macStyle`. This is a property defined in the [header table][3] of the file which specifies whether the font is regular, italic, bold, or a combination of the three. 

Adding a font to the Android system with an incorrect `macStyle` will cause the device to bootloop, resulting in angry users. Litho prevents this from happening by verifying that fonts have correctly assigned `macStyles`, and fixing the property in the case that it has been set incorrectly.

Download
--------
Visit the [releases page][5] and download the latest version.

Usage
-----
* Place your fonts in a folder with a suitable name.
* All files must be .TTF and styles should be [named exactly as shown here][4]

Example usage for a folder named `Helvetica`:
```bash
# Checks that each font has been assigned the correct macStyle
./litho verify Helvetica

# Assigns the correct macStyle to each font
./litho fix Helvetica

# Generates missing styles to build a full font package
./litho package Helvetica
``` 
This will create a folder named `HelveticaFontPack` containing all twelve verified styles.

License
--------

    Copyright 2015 Priyesh Patel

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://github.com/ItsPriyesh/Fontster
[2]: https://github.com/ItsPriyesh/Litho/blob/master/src/me/priyesh/litho/core/FontStyle.scala#L37-L44
[3]: https://www.microsoft.com/typography/otspec/head.htm
[4]: https://github.com/ItsPriyesh/Litho/blob/master/src/me/priyesh/litho/core/FontStyle.scala#L62-L75
[5]: https://github.com/ItsPriyesh/Litho/releases
