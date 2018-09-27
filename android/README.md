Rendering Structured Text Into Text in Android
==============================================

Rendering structured text in Android can be achieved in two ways: Either by rendering to a CharSequence of Spannables, 
or by rendering the native views. This document will describe both ways, and the dependencies for it.

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
  implementation 'com.github.contentful:structured-text-renderer.java:master-SNAPSHOT'
  implementation 'com.github.contentful:contentful.java:add~structured-text-SNAPSHOT'
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
	    <version>add~structured-text-SNAPSHOT</version>
	</dependency>
	<dependency>
	    <groupId>com.github.contentful</groupId>
	    <artifactId>structured-text-renderer.java</artifactId>
	    <version>master-SNAPSHOT</version>
	</dependency>
```

Calling Contentful Main SDK
---------------------------

Now that the base SDK is in place, the next step is to retrieve (_fetch_) an entry from Contentful,
containing Structured Text Data. Following code snippet does this by using `SPACE_ID`, `TOKEN` and
`ENTRY_ID` as placeholders for the actual content that needs fetching.

```java
final CDAClient client = new CDACllient.builder()
  .setSpace(SPACE_ID)
  .setToken(TOKEN);

final CDAEntry entry = client
  .fetch(CDAEntry.class)
  .one(ENTRY_ID);
```

With the `entry` at hand, getting the `CDAStructuredTextNode`, the base of all Structured Text 
nodes in the main SDK, is is easy, if the field id is known:

```java
final CDAStructuredTextNode node = entry.getField(FIELD_ID);
```

The last step includes the conversion of the structured text node into wither spannables or custom views:


> SPANNABLES

> CUSTOM VIEWS

Adding Custom Renderers
-----------------------

If a change of the output is wanted, adding of a new renderer or overriding a default one is needed.
To do so, using the `.addRenderer(…,…)` or `.addRendererUpFront(…,…)` method are needed. The 
Processor contains a list of renderers, which is iterated upon to find one matching the current 
Node encountered. For matching a renderer to a node, a `Checker` needs to be provided while adding
a `Renderer` to the `Processor`. 

```java
processor.addRenderer(
  new AndroidRenderer() {
      boolean check(@Nullable C context, @Nonnull CDAStructuredNode node) {
        return true;        
      }

      @Nullable R render(
          @Nonnull C context,
          @Nonnull CDAStructuredNode node) {
        return "";
          }
    }
);
```

The now added renderer will be working as a fallback: Since it got added last (by not using 
`.addRendererUpFront(…)`) it will get called last in the search of a renderer. It's checker is 
setup to always return true, so this being the last, the renderer will always get called. The 
renderer in the above example is simply returning the `.toString()` output of the given node.


Overriding Default Renderers
----------------------------

If overriding one of the default renderer is desired, the just presented way will not be sufficient:
Adding a new renderer to override an existing one will not get triggered, since the to be overriden
renderer will be checked before the just added one. For those kind of challenges, the 
`.addRendererUpFront(…)` method got added: It will move the renderer and checker to the front of
the list of renderer and will therefore be checked first. If the checker does not return true, the 
default renderer will be used. Upon returning the checker returning true, the current search for a
renderer will be aborted and the found renderer be used.

For inspiration on how custom renderer might look like, 
[a look at the source of the default renderer](src/main/java/com/contentful/structured/android/renderer)
 is recommended.