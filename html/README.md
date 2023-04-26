Rendering Rich Text Into HTML
===================================

For retrieving html output from a given rich text field this rendering libaray includes a snapshot release of the
Contentful Delivery SDK for Java.

Adding dependencies
-------------------

For gradle, adding this to `build.gradle` is needed, to allow base SDK snapshots and rendering SDK
dependencies to be found:

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
  implementation 'com.github.contentful:contentful.java:v10.5.6'
  implementation 'com.github.contentful.rich-text-renderer-java:html:1.6.0'
}
```

same can be achieved by adding Maven dependencies like so:

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
	    <groupId>com.github.contentful</groupId>
	    <artifactId>contentful.java</artifactId>
	    <version>v10.5.12</version>
	</dependency>
   <dependency>
        <groupId>com.github.contentful.rich-text-renderer-java</groupId>
        <artifactId>html</artifactId>
        <version>1.6.0</version>
   </dependency>
```

Calling Contentful Main SDK
---------------------------

Now that the base SDK is in place, the next step is to retrieve (_fetch_) an entry from Contentful,
containing Rich Text Data. Following code snippet does this by using `SPACE_ID`, `TOKEN` and
`ENTRY_ID` as placeholders for the actual content that needs fetching. Code needs to run in a background thread.

```java
final CDAClient client = CDAClient.builder()
    .setSpace(SPACE_ID)
    .setToken(TOKEN)
    .build();

final CDAEntry entry = client
  .fetch(CDAEntry.class)
  .one(ENTRY_ID);
```

With the `entry` at hand, getting the `CDARichDocument`, the base of all Rich Text
nodes in the main SDK, is easy if the field id is known:

```java
final CDARichDocument node = entry.getField(FIELD_ID);
```

The last step includes the conversion of the rich text node to an html string:

```java
final HtmlProcessor processor = new HtmlProcessor();
final HtmlContext context = new HtmlContext();
final String html = processor.process(context, node);
```

The `html` variable now contains the html representation of the node.

Adding Custom Renderers
-----------------------

To change the output, adding of a new renderer or overriding one is needed.
The `.addRenderer(…,…)` method exists to add a new renderer, and `.overrideRenderer(…,…)` to override an existing renderer. The
Processor contains a list of renderers, which is iterated upon to find one matching the current
Node. For matching a renderer to a node, a `Checker` needs to be provided when a `Renderer` gets added to the `Processor`.

```java
processor.addRenderer(
    (context, node) -> true, // Checker: Does the renderer need to be invoked?
    (context, node) -> node.toString() // Renderer, rendering specific node.
)
```

The now added renderer will be working as a fallback. Since it got added last (by using `.addRenderer(…,…)` instead of
`.overrideRenderer(…,…)`) it will get called last in the search of a renderer. It's checker is
setup to always return true, so this being the last, the renderer will always get called. The
renderer in the above example is simply returning the `.toString()` output of the given node.

A more sorrow check of the type of the node for the `Checker` and a better rendering of the error
is advised, since those presented here are just for demonstration purposes and don't do null
checking or more elaborated transformation of nodes.

Overriding Default Renderers
----------------------------

The example above will not work to override a default renderer, the to-be-overridden renderer will be checked before
the new one. For those kind of challenges, use the `.overrideRenderer(…,…)` method: It will move the renderer
and checker to the front of the list of renderer and will therefore be checked first. If the checker does not return
true, the default renderer will be used. Upon returning the checker returning true, the current search for a
renderer will be aborted and the found renderer be used.

For inspiration on how custom renderer might look like,
[a look at the source of the default renderer](src/main/java/com/contentful/rich/html/renderer)
 is recommended.
