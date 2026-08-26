<p align="center">
  <a href="https://www.contentful.com/slack/">
    <img src="https://img.shields.io/badge/-Join%20Community%20Slack-2AB27B.svg?logo=slack&maxAge=31557600" alt="Join Contentful Community Slack">
  </a>
  &nbsp;
  <a href="https://www.contentfulcommunity.com/">
    <img src="https://img.shields.io/badge/-Join%20Community%20Forum-3AB2E6.svg?logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA1MiA1OSI+CiAgPHBhdGggZmlsbD0iI0Y4RTQxOCIgZD0iTTE4IDQxYTE2IDE2IDAgMCAxIDAtMjMgNiA2IDAgMCAwLTktOSAyOSAyOSAwIDAgMCAwIDQxIDYgNiAwIDEgMCA5LTkiIG1hc2s9InVybCgjYikiLz4KICA8cGF0aCBmaWxsPSIjNTZBRUQyIiBkPSJNMTggMThhMTYgMTYgMCAwIDEgMjMgMCA2IDYgMCAxIDAgOS05QTI5IDI5IDAgMCAwIDkgOWE2IDYgMCAwIDAgOSA5Ii8+CiAgPHBhdGggZmlsbD0iI0UwNTM0RSIgZD0iTTQxIDQxYTE2IDE2IDAgMCAxLTIzIDAgNiA2IDAgMSAwLTkgOSAyOSAyOSAwIDAgMCA0MSAwIDYgNiAwIDAgMC05LTkiLz4KICA8cGF0aCBmaWxsPSIjMUQ3OEE0IiBkPSJNMTggMThhNiA2IDAgMSAxLTktOSA2IDYgMCAwIDEgOSA5Ii8+CiAgPHBhdGggZmlsbD0iI0JFNDMzQiIgZD0iTTE4IDUwYTYgNiAwIDEgMS05LTkgNiA2IDAgMCAxIDkgOSIvPgo8L3N2Zz4K&maxAge=31557600" alt="Join Contentful Community Forum">
  </a>
</p>

# rich-text-renderer-java - Rich Text Rendering for Contentful on the JVM

> Java library for rendering Contentful [Rich Text](https://www.contentful.com/developers/docs/concepts/rich-text/) fields. It helps you easily render rich text stored in Contentful using Java, targeting both plain JVM applications (HTML output) and Android (Spannables or native Views).

<p align="center">
  <img src="https://img.shields.io/badge/Status-Maintained-green.svg" alt="This repository is actively maintained" />
  &nbsp;
  <a href="https://jitpack.io/#contentful/rich-text-renderer-java">
    <img src="https://jitpack.io/v/contentful/rich-text-renderer-java.svg" alt="JitPack">
  </a>
</p>

**What is Contentful?**

[Contentful](https://www.contentful.com/) provides content infrastructure for digital teams to power websites, apps, and devices. Unlike a CMS, Contentful was built to integrate with the modern software stack. It offers a central hub for structured content, powerful management and delivery APIs, and a customizable web app that enable developers and content creators to ship their products faster.

<details open>
<summary>Table of contents</summary>
<!-- TOC -->

- [rich-text-renderer-java - Rich Text Rendering for Contentful on the JVM](#rich-text-renderer-java---rich-text-rendering-for-contentful-on-the-jvm)
  - [Core Features](#core-features)
  - [Getting started](#getting-started)
    - [Requirements](#requirements)
    - [Installation](#installation)
  - [Using the SDK](#using-the-sdk)
    - [Rendering to HTML](#rendering-to-html)
    - [Rendering on Android](#rendering-on-android)
    - [Extending with custom renderers](#extending-with-custom-renderers)
  - [Documentation & References](#documentation--references)
  - [Reach out to us](#reach-out-to-us)
    - [Have questions about how to use this library?](#have-questions-about-how-to-use-this-library)
    - [You found a bug or want to propose a feature?](#you-found-a-bug-or-want-to-propose-a-feature)
  - [Get involved](#get-involved)
    - [Development setup](#development-setup)
  - [License](#license)
  - [Code of Conduct](#code-of-conduct)

<!-- /TOC -->

</details>

## Core Features

- Takes the rich text node tree produced by [contentful.java](https://github.com/contentful/contentful.java) (`CDARichDocument` and friends) and renders it into a representation that's easy to use in your own project.
- `html` module: converts rich text into an HTML string, suitable for any JVM application (web backends, CLI tools, etc).
- `android` module: converts rich text into either `CharSequence`/`Spannable` output for `TextView`, or native Android `View` hierarchies — your choice.
- A shared `core` module defines the `Processor`/`Renderer`/`RenderabilityChecker` pattern used by both the `html` and `android` modules, and is dependency-free of either.
- Fully extensible: add a new `Renderer` to a `Processor` via `.addRenderer(…)`, or replace a default one via `.overrideRenderer(…)`, without forking the library.
- `Simplifier` utilities to clean up a rich text graph before rendering — for example removing empty nodes or nodes nested beyond a given depth (`RemoveToDeepNesting`), useful for capping Android rendering time on deeply nested documents.
- Ships as three independently versioned JitPack artifacts (`core`, `html`, `android`) so you only pull in what you need.

## Getting started

- [Requirements](#requirements)
- [Installation](#installation)

### Requirements

| Requirement | Version |
| --- | --- |
| Java | 17 |
| Android (for the `android` module) | compileSdk 35, minSdk 23 |

This library depends on [contentful.java](https://github.com/contentful/contentful.java) for the `CDARichDocument` node model that it renders.

### Installation

Artifacts are distributed via [JitPack](https://jitpack.io/#contentful/rich-text-renderer-java). Add the JitPack repository, then depend on the module(s) you need:

* _Gradle_

```groovy
allprojects {
  repositories {
    // …
    maven { url 'https://jitpack.io' }
  }
}
```

```groovy
dependencies {
  // …
  implementation 'com.contentful.java:java-sdk:10.6.0'
  implementation 'com.github.contentful.rich-text-renderer-java:html:2.4.0'
  // or, for Android:
  implementation 'com.github.contentful.rich-text-renderer-java:core:2.4.0'
  implementation 'com.github.contentful.rich-text-renderer-java:android:2.4.0'
}
```

* _Maven_

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.contentful.java</groupId>
    <artifactId>java-sdk</artifactId>
    <version>10.6.0</version>
</dependency>
<dependency>
    <groupId>com.github.contentful.rich-text-renderer-java</groupId>
    <artifactId>html</artifactId>
    <version>2.4.0</version>
</dependency>
```

## Using the SDK

Both the `html` and `android` modules start the same way: fetch an entry containing a rich text field with the base [contentful.java](https://github.com/contentful/contentful.java) SDK, then extract the `CDARichDocument`:

```java
final CDAClient client = CDAClient.builder()
    .setSpace(SPACE_ID)
    .setToken(TOKEN)
    .build();

final CDAEntry entry = client
    .fetch(CDAEntry.class)
    .one(ENTRY_ID);

final CDARichDocument node = entry.getField(FIELD_ID);
```

If your rich text data comes from an external tool (for example, a JavaScript library) rather than directly through this SDK, build a `CDARichDocument` from plain JSON using GSON:

```java
private final Gson gson = new Gson();
Type type = new TypeToken<Map<String, Object>>(){}.getType();
Map<String, Object> jsonMap = gson.fromJson(json, type);
final CDARichDocument node = RichTextFactory.resolveRichNode(jsonMap);
```

### Rendering to HTML

Use the `html` module to convert the node tree into an HTML string:

```java
final HtmlProcessor processor = new HtmlProcessor();
final HtmlContext context = new HtmlContext();
final String html = processor.process(context, node);
```

See the [`html` module README](html/README.md) for the full guide, including custom renderers.

### Rendering on Android

Use the `android` module to convert the node tree into either `CharSequence`/`Spannable` output, or native `View`s:

```java
final AndroidProcessor<CharSequence> sequenceProcessor = AndroidProcessor.creatingCharSequences();
// or
final AndroidProcessor<View> viewProcessor = AndroidProcessor.creatingNativeViews();

final AndroidContext context = new AndroidContext(activity.getContext());

final CharSequence result = sequenceProcessor.process(context, node);
// or
final View result = viewProcessor.process(context, node);
```

See the [`android` module README](android/README.md) for the full guide, including rendering embedded entries/assets and hyperlinks, and performance advice for deeply nested documents.

### Extending with custom renderers

Extend the core functionality by adding a new `Renderer` to a `Processor`. A `Processor` holds a list of renderers; each one knows how to render one kind of rich text node. Adding a renderer with `.addRenderer(…)` appends it to the end of the list (lowest priority, used as a fallback); `.overrideRenderer(…)` prepends it (checked first, taking priority over the built-in renderer for that node type):

```java
processor.addRenderer(
    (context, node) -> true, // Checker: does the renderer need to be invoked?
    (context, node) -> node.toString() // Renderer: renders the specific node.
)
```

> Extending is especially important if you plan on rendering `embedded` or `hyperlink` rich text nodes: this library cannot know your content model in advance, so it never ships a default renderer for those node types. Always provide your own renderer for embedded entries, embedded assets, and hyperlinks.

> We are always looking for feedback — feel free to [create an issue](https://github.com/contentful/rich-text-renderer-java/issues/new).

## Documentation & References

This library is a companion to [contentful.java](https://github.com/contentful/contentful.java); consult its README for details on `CDAClient`, fetching entries, and the `CDARichDocument` node model this library renders. Every released change is recorded in the module-level source and JitPack release history.

## Reach out to us

### Have questions about how to use this library?

* Reach out to our community forum: [![Contentful Community Forum](https://img.shields.io/badge/-Join%20Community%20Forum-3AB2E6.svg?logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA1MiA1OSI+CiAgPHBhdGggZmlsbD0iI0Y4RTQxOCIgZD0iTTE4IDQxYTE2IDE2IDAgMCAxIDAtMjMgNiA2IDAgMCAwLTktOSAyOSAyOSAwIDAgMCAwIDQxIDYgNiAwIDEgMCA5LTkiIG1hc2s9InVybCgjYikiLz4KICA8cGF0aCBmaWxsPSIjNTZBRUQyIiBkPSJNMTggMThhMTYgMTYgMCAwIDEgMjMgMCA2IDYgMCAxIDAgOS05QTI5IDI5IDAgMCAwIDkgOWE2IDYgMCAwIDAgOSA5Ii8+CiAgPHBhdGggZmlsbD0iI0UwNTM0RSIgZD0iTTQxIDQxYTE2IDE2IDAgMCAxLTIzIDAgNiA2IDAgMSAwLTkgOSAyOSAyOSAwIDAgMCA0MSAwIDYgNiAwIDAgMC05LTkiLz4KICA8cGF0aCBmaWxsPSIjMUQ3OEE0IiBkPSJNMTggMThhNiA2IDAgMSAxLTktOSA2IDYgMCAwIDEgOSA5Ii8+CiAgPHBhdGggZmlsbD0iI0JFNDMzQiIgZD0iTTE4IDUwYTYgNiAwIDEgMS05LTkgNiA2IDAgMCAxIDkgOSIvPgo8L3N2Zz4K&maxAge=31557600)](https://support.contentful.com/)
* Jump into our community slack channel: [![Contentful Community Slack](https://img.shields.io/badge/-Join%20Community%20Slack-2AB27B.svg?logo=slack&maxAge=31557600)](https://www.contentful.com/slack/)

### You found a bug or want to propose a feature?

* File an issue here on GitHub: [![File an issue](https://img.shields.io/badge/-Create%20Issue-6cc644.svg?logo=github&maxAge=31557600)](https://github.com/contentful/rich-text-renderer-java/issues/new). Make sure to remove any credential from your code before sharing it.

## Get involved

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?maxAge=31557600)](http://makeapullrequest.com)

We appreciate any help on our repositories. See [CONTRIBUTING.md](CONTRIBUTING.md) for the full contributor workflow.

### Development setup

This is a multi-module Gradle project (`core`, `html`, `android`, `android_sample`). Use the included Gradle wrapper — do not install Gradle separately:

```bash
git clone git@github.com:contentful/rich-text-renderer-java.git
cd rich-text-renderer-java

# Build all modules.
./gradlew build

# Run all tests.
./gradlew test

# Build or test a specific module.
./gradlew :core:build
./gradlew :html:test
./gradlew :android:test
```

## License

This repository is published under the [MIT](LICENSE) license.

## Code of Conduct

We want to provide a safe, inclusive, welcoming, and harassment-free space and experience for all participants, regardless of gender identity and expression, sexual orientation, disability, physical appearance, socioeconomic status, body size, ethnicity, nationality, level of experience, age, religion (or lack thereof), or other identity markers.

[Read our full Code of Conduct](https://github.com/contentful-developer-relations/community-code-of-conduct).

<!-- Generated by seed-golden-context | Last updated: 2026-05-11 -->

## Documentation & Agent Context

| Document | What it covers |
|---|---|
| [ARCHITECTURE.md](./ARCHITECTURE.md) | Internal module structure, data flow, key dependencies, extension model |
| [CONTRIBUTING.md](./CONTRIBUTING.md) | Build setup, test commands, commit conventions, release process |
| [docs/ADRs/](./docs/ADRs/) | Why key decisions were made (distribution, module structure, renderer pattern) |
| [AGENTS.md](./AGENTS.md) | Agent-first context directory — sharp edges, invariants, quick reference |

## Integration Context

**Upstream (this repo consumes):**
- [contentful.java SDK](https://github.com/contentful/contentful.java) (`com.contentful.java:java-sdk`) — provides the `CDARichNode` type hierarchy

**Downstream (consumes this repo):**
- JVM and Android applications that render Contentful Rich Text fields
- Distributed via [JitPack](https://jitpack.io/#contentful/rich-text-renderer-java)
