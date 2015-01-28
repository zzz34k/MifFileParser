Mif File Parser
===============

Parser of Mif files, where Mif = MapInfo Interchange Format (not the Adobe file format).

The purpose is to transform such file into a meta data structure, then to export it back to a text file format.

Currently supported export formats are Mif (to check it was properly parsed) and Json (export of geometry in a SVG path style).

Not all the Mif specification is supported yet, but there is already a substantial subset, enough for the purpose it has been created for.

TODO:
- Support viewport settings, as some maps are upside-down.
- Write proper unit tests! It has been tested on real Mif files, though.
