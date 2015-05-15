[![License](https://img.shields.io/github/license/LordAkkarin/BaseDefense2.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![Latest Tag](https://img.shields.io/github/tag/LordAkkarin/BaseDefense2.svg?style=flat-square&label=Latest Tag)](https://github.com/LordAkkarin/BaseDefense2/tags)
[![Latest Release](https://img.shields.io/github/release/LordAkkarin/BaseDefense2.svg?style=flat-square&label=Latest Release)](https://github.com/LordAkkarin/BaseDefense2/releases)

Base Defense 2
==============

Table of Contents
-----------------
* [About](#about)
* [Contacts](#contacts)
* [License](#license)
* [Downloads](#downloads)
* [Installation](#installation)
* [Dependencies](#dependencies)
* [Issues](#issues)
* [Building](#building)
* [Contributing](#contributing)
* [API](#api)
* [Localization](#localization)
* [Credits](#credits)

About
-----

A modification about protecting mad scientists against evil people that try to stop them from gaining world domination.

Contacts
--------

* [Website](https://mcbd.spud.rocks)
* [IRC #Akkarin on irc.spi.gt](http://irc.spi.gt/iris/?nick=Guest....&channels=Akkarin&prompt=1) (alternatively #Akkarin on esper.net)
* [GitHub](https://github.com/LordAkkarin/BaseDefense2)

License
-------

* Base Defense 2 API
  - Copyright (C) 2015 Johannes "Akkarin" Donath
  - [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.txt)
* Base Defense 2
  - Copyright (C) 2015 Johannes "Akkarin" Donath
  - [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.txt)
* Textures & Models
  - Copyright (C) 2015 Johannes "Akkarin" Donath
  - [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.txt)
* Promotional Material
  - Copyright (C) 2015 Johannes "Akkarin" Donath
  - [![License](https://img.shields.io/badge/License-CC%20BY--SA-red.svg?style=flat-square)](http://creativecommons.org/licenses/by-sa/4.0/)
* Text & Translations
  - Copyright (C) 2015 Johannes "Akkarin" Donath
  - [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.txt)

Downloads
---------

Released versions of the modification can be found on [GitHub](https://github.com/LordAkkarin/BaseDefense2/releases),
[Patreon](https://www.patreon.com/Akkarin) and the [official Project Website](https://mcbd.spud.rocks).

Installation
------------

The modification is installed by dropping the BaseDefense-2.0-universal.jar into your ```mods``` folder. Please also
make sure that all [dependencies](#dependencies) are present before starting the game.

Dependencies
------------

* [Minecraft Forge for 1.7.10](http://files.minecraftforge.net/) (10.13.3.1403 and newer recommended)
* [Applied Energistics 2](https://github.com/AppliedEnergistics/Applied-Energistics-2)

Issues
------

You encountered problems with the mod or have a suggestion? Create an issue!

1. Make sure your issue has not been fixed in a newer version (check the list of [closed issues](https://github.com/LordAkkarin/BaseDefense2/issues?q=is%3Aissue+is%3Aclosed)
1. Create [a new issue](https://github.com/LordAkkarin/BaseDefense2/issues/new) from the [issues page](https://github.com/LordAkkarin/BaseDefense2/issues)
1. Enter your issue's title (something that summarizes your issue) and create a detailed description containing:
   - What is the expected result?
   - What problem occurs?
   - How to reproduce the problem?
   - Server or Single-Player?
   - Modpack and Version?
   - Crash Log (Please use a [Pastebin](http://www.pastebin.com) service)
   - Screenshots (if possible)
1. Click "Submit" and wait for further instructions

Building
--------

1. Clone this repository via ```git clone https://github.com/LordAkkarin/BaseDefense2.git``` or download a [zip](https://github.com/LordAkkarin/BaseDefense2/archive/master.zip)
1. Build the modification by running ```./gradlew build``` (or ```./gradlew.bat build``` on Windows)
1. The resulting jars can be found in ```build/libs```

To prepare a development environment you will need to run these additional commands:
1. ```./gradlew setupDecompWorkspace``` (or ```./gradlew.bat setupDecompWorkspace``` on Windows)
1. ```./gradlew idea``` (or ```./gradlew.bat idea```) for IntelliJ users and ```./gradlew eclipse``` (or ```./gradlew.bat eclipse``` on Windows) for Eclipse users

Contributing
------------

Before you add any major changes to the modification you may want to discuss them with us (see [Contact](#contact)) as
we may choose to reject your changes for various reasons. All contributions are applied via [Pull-Requests](https://help.github.com/articles/creating-a-pull-request).
Patches will not be accepted. Also be aware that all of your contributions are made available under the terms of the
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt). Please read the [Contribution Guidelines](Contributing.md)
for more information.

API
---
The BaseDefense 2 API allows developers to extend the modification functionality from within their mods. Copies of the
API may be acquired by building this project (see [Building](#building)) or by downloading a build from one of the
official sources (see [Downloads](#downloads)).

A maven repository is not (yet) available to the public. Please check back later.

Localization
------------

You may add new translations to the modification by creating a file with the [appropriate language code](http://download1.parallels.com/SiteBuilder/Windows/docs/3.2/en_US/sitebulder-3.2-win-sdk-localization-pack-creation-guide/30801.htm)
(please refer to GitHub's [help article on Pull-Requests](https://help.github.com/articles/creating-a-pull-request) for
more information on how to merge new localization files.

Please note that all translation files **MUST BE** encoded as UTF-8. Fixes to existing translations are welcome.

Credits
-------

* The Mojang Team for Minecraft
* Lex and all of his hard-working helpers for Minecraft Forge and the Forge Mod Loader
* AlgorithmX2 and [all Contributors](https://github.com/AppliedEnergistics/Applied-Energistics-2/graphs/contributors) for Applied Energistics 2
* [All Contributors](https://github.com/LordAkkarin/BaseDefense2/graphs/contributors) to this Modification
