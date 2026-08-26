# Rendering Rich Text into HTML

> Part of [rich-text-renderer-java](https://github.com/contentful/rich-text-renderer-java). This module converts a Contentful `CDARichDocument` into an HTML string, suitable for any JVM application.

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
  implementation 'com.github.contentful.rich-text-renderer-java:html:2.4.0'
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
    <artifactId>html</artifactId>
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

## Rendering to HTML

Convert the rich text node into an HTML string:

```java
final HtmlProcessor processor = new HtmlProcessor();
final HtmlContext context = new HtmlContext();
final String html = processor.process(context, node);
```

The `html` variable now contains the HTML representation of the node.

## Adding custom renderers

To change the output, add a new renderer or override an existing one. Use `.addRenderer(…, …)` to add a new renderer, or `.overrideRenderer(…, …)` to override an existing one. The `Processor` holds a list of renderers that it iterates to find one matching the current node. For matching a renderer to a node, provide a `Checker` when adding a `Renderer` to the `Processor`:

```java
processor.addRenderer(
    (context, node) -> true, // Checker: does the renderer need to be invoked?
    (context, node) -> node.toString() // Renderer, rendering the specific node.
)
```

The renderer added above acts as a fallback. Since it was added last (via `.addRenderer(…, …)` instead of `.overrideRenderer(…, …)`), it's checked last. Its checker always returns `true`, so being last in line, it always gets called. This example renderer simply returns the `.toString()` output of the given node.

A more thorough type check in the `Checker`, and more elaborate error handling in the `Renderer`, is recommended — the code above is for demonstration purposes only and doesn't do null checking or proper node transformation.

## Overriding default renderers

The example above won't override a default renderer, since the default renderer is checked before the newly added one. For that, use `.overrideRenderer(…, …)`: it moves the renderer and checker to the front of the list, so it's checked first. If the checker returns `false`, the default renderer is used instead. If the checker returns `true`, the search for a renderer stops and the new renderer is used.

For inspiration on writing a custom renderer, take a look at [the source of the default renderers](src/main/java/com/contentful/rich/html/renderer).

> Overriding a renderer is required if you want to provide your own representation for `embedded` or `hyperlink` rich text nodes — this library cannot know what your content model looks like, so it never ships a default renderer for those node types.
