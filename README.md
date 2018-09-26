Contentful Structured Text Rendering SDK for Java
=================================================

[![Build Status](https://travis-ci.org/contentful/structured-text-rendering.java.svg)](https://travis-ci.org/contentful/structured-text-rendering.java/builds#)
[![codecov](https://codecov.io/gh/contentful/structured-text-rendering.java/branch/master/graph/badge.svg)](https://codecov.io/gh/contentful/structured-text-rendering.java)


> Java SDK for [Structured Text API](https://www.contentful.com/developers/docs/tutorials/general/structured-text-field-type-alpha/) . It helps in easily rendering structured text stored in Contentful using Java.


What is Contentful?
-------------------
[Contentful](https://www.contentful.com) provides a content infrastructure for digital teams to power content in websites, apps, and devices. Contentful, unlike any other CMS, is built to integrate with the modern software stack. It offers a central hub for structured content, powerful management and delivery APIs, and a customizable web app that enable developers and content creators to ship digital products faster.

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

Take structured text elements from the [BaseSdk](https://github.com/contentful/contentful.java) and render them in a representation easy to use and understand and even easier to use in own projects, due to these submodules:

Html
====

For rendering structured text to html, please take a look at the modules [README.md](html/README.md)

Android
=======

For further understanding on how to use structured text in Android, please take a look at the modules [README.md](android/README.md)


Extensions
==========

For some use cases it is recommended thinking about extending the core functionallity by adding new `Renderer` to the `Processor`. A Processor is the structure containing several renderer. Every renderer does know excatly how to render one part of the structured text api. By adding a renderer to the processor, you can extend the functionality of either one of the core processors, or you could even create your own one. For adding a new renderer you first need to create a Processor, and then call its `addRenderer()` method. If you want to override existing renderer, please use the `.addRendererUpFront(…)`- method, because otherwise the default renderer added will take over and the custom renderer might not even have a chance on getting asked.

> We are always looking for feedback, so if you have some, feel free to create an [issue for us](https://github.com/contentful/structured-text-renderer.java/issues/new).