<p align="center">
  🚧🚧🚧🚧🏗️👷🚧🚧👨‍💻🚧🚧👷🏗️🚧🚧🚧🚧<br>
  <b>
    THIS REPOSITORY IS IN BETA, FEEDBACK WANTED.<br>
    EXPECT HEAVY CHANGES AND SUBOPTIMAL VISUAL QUALITY
  </b>
  <br>
  🚧🚧🚧🚧🏗️👷🚧🚧👨‍💻🚧🚧👷🏗️🚧🚧🚧🚧<br>
  <br>
  <sup>Please follow https://github.com/contentful/rich-text-renderer.java/issues/2 for details.</sup>
</p>


Contentful Rich Text Rendering SDK for Java
===========================================

[![Build Status](https://travis-ci.org/contentful/rich-text-rendering.java.svg)](https://travis-ci.org/contentful/rich-text-rendering.java/builds#)
[![codecov](https://codecov.io/gh/contentful/rich-text-rendering.java/branch/master/graph/badge.svg)](https://codecov.io/gh/contentful/rich-text-rendering.java)


> Java SDK for [Rich Text API](https://www.contentful.com/developers/docs/tutorials/general/rich-text-field-type-alpha/) . It helps in easily rendering rich text stored in Contentful using Java.


What is Contentful?
-------------------
[Contentful](https://www.contentful.com) provides a content infrastructure for digital teams to power content in websites, apps, and devices. Contentful, unlike any other CMS, is built to integrate with the modern software stack. It offers a central hub for rich content, powerful management and delivery APIs, and a customizable web app that enable developers and content creators to ship digital products faster.

<details open>
<summary>Table of contents</summary>

<!-- TOC -->
- [Core Features](#core-features)
  - [Html](#html)
  - [Android](#android)
- [Extensions](#extensions)

</details>

<!-- /TOC -->

Core Features
=============

Take rich text elements from the [BaseSdk](https://github.com/contentful/contentful.java) and render them in a representation easy to use and understand and even easier to use in own projects, due to these submodules:

Html
====

For rendering rich text to html, please take a look at the modules [README.md](html/README.md)

Android
=======

For further understanding on how to use rich text in Android, please take a look at the modules [README.md](android/README.md)


Extensions
==========

Extending the core functionality can be achieved, by adding a new `Renderer` to the `Processor`. A Processor is the structure containing several renderer. Every renderer knows exactly how to render one part of the rich text api. By adding a renderer to the processor, the functionality of either one of the core processors can be extended. For adding a new renderer calling a processors `.addRenderer()` method is needed. If existing should be overwritten renderer, use the `.addRendererUpFront(…)`- method, otherwise the default renderer added will take over and the custom renderer gets ignored.

Extending is especially needed, if you are planing on using some of the `Embedded` or `Hyperlink` Rich Nodes: Those cannot generate a generic representation, since these libraries cannot know how to render specific representations. In those cases it is advised to use custom renderers.

> We are always looking for feedback, so if you have some, feel free to create an [issue for us](https://github.com/contentful/rich-text-renderer.java/issues/new).