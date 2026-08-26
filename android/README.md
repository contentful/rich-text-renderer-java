# Rendering Rich Text into Text in Android

> Part of [rich-text-renderer-java](https://github.com/contentful/rich-text-renderer-java). This module renders a Contentful `CDARichDocument` on Android — either as a `CharSequence` of Spannables, or as native Android Views. This document covers both approaches and their dependencies.

## Installation

For Gradle, add the JitPack repository to allow base SDK and rendering SDK dependencies to be found:

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
  implementation 'com.github.contentful.rich-text-renderer-java:android:2.4.0'
  implementation 'com.github.contentful.rich-text-renderer-java:core:2.4.0'
}
```

The same can be achieved with Maven:

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
    <artifactId>core</artifactId>
    <version>2.4.0</version>
</dependency>
<dependency>
    <groupId>com.github.contentful.rich-text-renderer-java</groupId>
    <artifactId>android</artifactId>
    <version>2.4.0</version>
</dependency>
```

## Fetching content with the Contentful SDK

With the base SDK in place, fetch (`fetch`) an entry from Contentful containing rich text data. The following snippet uses `SPACE_ID`, `TOKEN`, and `ENTRY_ID` as placeholders for the actual content that needs fetching. Run it on a background thread:

```java
final CDAClient client = CDAClient.builder()
    .setSpace(SPACE_ID)
    .setToken(TOKEN)
    .build();

final CDAEntry entry = client
  .fetch(CDAEntry.class)
  .one(ENTRY_ID);
```

With the `entry` at hand, retrieve the `CDARichDocument` — the base of all rich text nodes in the main SDK — if the field id is known:

```java
final CDARichDocument node = entry.getField(FIELD_ID);
```

If your data is fetched from an external tool (for example a JavaScript library), you can build a `CDARichDocument` from plain JSON instead. This is especially useful when working with content that wasn't fetched directly through the Contentful Java library. Using GSON for JSON processing:

```java
private final Gson gson = new Gson();
Type type = new TypeToken<Map<String, Object>>(){}.getType();
Map<String, Object> jsonMap = gson.fromJson(json, type);
final CDARichDocument node = RichTextFactory.resolveRichNode(jsonMap);
```

## Rendering to Spannables or native Views

Convert the rich text node into either spannables or custom views. The following code creates the two available processor types:

```java
final AndroidProcessor<CharSequence> sequenceProcessor = AndroidProcessor.creatingCharSequences();
// or
final AndroidProcessor<View> viewProcessor = AndroidProcessor.creatingNativeViews();
```

With those instances at hand, create a context and render the desired output:

```java
final AndroidContext context = new AndroidContext(activity.getContext());

final CharSequence result = sequenceProcessor.process(context, node);
// or
final View result = viewProcessor.process(context, node);
```

## Adding custom renderers

To change the output, add a new renderer or override a default one, using `.addRenderer(…, …)` or `.overrideRenderer(…, …)`. The `Processor` holds a list of renderers that it iterates to find one matching the current node. For matching a renderer to a node, provide a `Checker` when adding a `Renderer` to the `Processor`:

```java
processor.addRenderer(
  new AndroidRenderer() {
      boolean check(@Nullable C context, @Nonnull CDARichNode node) {
        return true;
      }

      @Nullable R render(
          @Nonnull C context,
          @Nonnull CDARichNode node) {
        return "";
          }
    }
);
```

The renderer added above acts as a fallback: since it was added last (without using `.overrideRenderer(…)`), it's checked last. Its checker always returns `true`, so being last in line, it always gets called. The example renderer simply returns an empty string.

## Overriding default renderers

If you want to override one of the default renderers, the approach above won't work: adding a new renderer to override an existing one won't get triggered, since the to-be-overridden renderer is checked before the newly added one. For that, use `.overrideRenderer(…)`: it moves the renderer and checker to the front of the list, so it's checked first. If the checker returns `false`, the default renderer is used instead. If the checker returns `true`, the search for a renderer stops and the new renderer is used.

For inspiration on writing a custom renderer, take a look at [the source of the default renderers](src/main/java/com/contentful/rich/android/renderer).

> Overriding a renderer is required if you want to provide embedded or hyperlinked views of your content. Contentful cannot know what your content model looks like, so it never ships a default renderer for `embedded` or `hyperlink` nodes. Take a look at the companion sample Android app for a full example.

## Android specifics for Rich Text rendering

This section covers general advice for using the accompanying rich text renderer on Android.

### Rendering times

Rendering native Android views can get time-consuming, especially with deeply nested rich text documents. Use the `RemoveToDeepNesting` simplifier to remove all elements below a given nesting level before rendering.

### Rendering embedded content and hyperlinks

Every custom visualization must be defined by the client application. If you need a hyperlink or an embedded link to an Entry or an Asset, add a renderer as described above. Contentful cannot know your use case in advance — especially what an entry's fields should look like (which field to use for a description, what's important to display, etc) — so this library relies on you to render that custom material.
