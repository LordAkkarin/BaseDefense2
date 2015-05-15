How to Contribute
=================

Contributions are an important aspect of Base Defense and allow us to make future versions of this modification even
better! Everybody can submit changes including (but obviously not limited to): new features, bugfixes, textures and
translations.

Getting Started
---------------

* Make sure you have a [GitHub Account](https://github.com/signup/free)
* Make sure you signed the [CLA](https://www.clahub.com/agreements/LordAkkarin/BaseDefense2)
* Carefully read the [Guidelines](#guidelines)
* Fork the repository on GitHub

Making Changes
--------------

* Create a topic branch where you want to base your work
  - This is usually the master branch
  - Only target release branches if you are certain your fix must be on that branch
  - To quickly create a master-based branch run ```git checkout -b my/fix/branch master```. Please avoid working on the ```master``` branch.
* Make commits of logical units
* Check for unnecessary whitespace and changes to the newlines with ```git diff --check``` before committing

Submitting Changes
------------------
* Sign the [Contributor License Agreement](https://www.clahub.com/agreements/LordAkkarin/BaseDefense2)
* Push your changes to a topic branch in your fork of the repository
* Submit a pull request to the repository
* The Core Team reviews Pull-Requests on a regular basis
* After feedback has been given we expect responses within two weeks. After two weeks we may close the pull request if it isn't showing any activity.

Code Guidelines
---------------
* **Charset:** UTF-8
* **Indentation:** 8 Spaces
* **New Lines:** UNIX Line Endings; One extra newline at the end of the file
* **Trailing Whitespaces** are removed
* **Braces:** On the same line where possible
* **Simple Statements** may be on one line
* **Less levels** are preferred (use return, break and continue frequently)

Note: Most of these guidelines can be automatically applied via [EditorConfig](http://editorconfig.org/).

License Header
--------------
All new files are **REQUIRED** to include the following license header:

```java
/*
 * Copyright YYYY Your Name <your@email.address>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```
