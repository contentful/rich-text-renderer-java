Rendering Rich Text Into Text in Android
==============================================

Rendering rich text in Android can be achieved in two ways: Either by rendering to a CharSequence of Spannables,
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
  implementation 'com.contentful.java:java-sdk:10.5.18'
  implementation 'com.github.contentful.rich-text-renderer-java:android:2.1.0'
  implementation 'com.github.contentful.rich-text-renderer-java:core:2.1.0'
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
        <groupId>com.contentful.java</groupId>
        <artifactId>java-sdk</artifactId>
        <version>10.5.18</version>
   </dependency>
   <dependency>
       <groupId>com.github.contentful.rich-text-renderer-java</groupId>
       <artifactId>core</artifactId>
       <version>2.1.0</version>
   </dependency>
   <dependency>
       <groupId>com.github.contentful.rich-text-renderer-java</groupId>
       <artifactId>android</artifactId>
       <version>2.1.0</version>
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
If your data is fetched from an external tool (e.g., a JavaScript library), it is possible to create a `CDARichDocument` from plain `JSON`. This is especially useful when working with content that is not directly retrieved through the Contentful Java library. To accomplish this, the following steps can be followed using the `GSON` library for `JSON` processing:

```java
private final Gson gson = new Gson();
Type type = new TypeToken<Map<String, Object>>(){}.getType();
Map<String, Object> jsonMap = gson.fromJson(json, type);
final CDARichDocument node =  RichTextFactory.resolveRichNode(jsonMap);
```

The last step includes the conversion of the rich text node into wither spannables or custom views. Following code shows
how to create renderers to create charsequences or native android views:

```java
final AndroidProcessor<CharSequence> sequenceProcessor = AndroidProcessor.creatingCharSequences();
// or
final AndroidProcessor<View> viewProcessor = AndroidProcessor.creatingNativeViews();
```

With those two instances at hand, you can create a context and render the desired output:

```java
final AndroidContext context = new AndroidContext(activity.getContext());

final CharSequence result = sequenceProcessor.process(context, node);
// or
final View result = viewProcessor.process(context, node);
```

Adding Custom Renderers
-----------------------

If a change of the output is wanted, adding of a new renderer or overriding a default one is needed.
To do so, using the `.addRenderer(…,…)` or `.overrideRenderer(…,…)` method are needed. The
Processor contains a list of renderers, which is iterated upon to find one matching the current
Node encountered. For matching a renderer to a node, a `Checker` needs to be provided while adding
a `Renderer` to the `Processor`.

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

The now added renderer will be working as a fallback: Since it got added last (by not using
`.overrideRenderer(…)`) it will get called last in the search of a renderer. It's checker is
setup to always return true, so this being the last, the renderer will always get called. The
renderer in the above example is simply returning the `.toString()` output of the given node.


Overriding Default Renderers
----------------------------

If overriding one of the default renderer is desired, the just presented way will not be sufficient:
Adding a new renderer to override an existing one will not get triggered, since the to be overridden
renderer will be checked before the just added one. For those kind of challenges, the
`.overrideRenderer(…)` method got added: It will move the renderer and checker to the front of
the list of renderer and will therefore be checked first. If the checker does not return true, the
default renderer will be used. Upon returning the checker returning true, the current search for a
renderer will be aborted and the found renderer be used.

For inspiration on how custom renderer might look like,
[a look at the source of the default renderer](src/main/java/com/contentful/rich/android/renderer)
 is recommended.

> Overriding of renderers is needed, if you want to provide embedded or hyperlinked views of your content. Sadly
> Contentful does not know what your content is like, and cannot create an embedded view on it. Look at the companion
> sample android app is advised.


Android specifics for Rich Text rendering
-----------------------------------------

This section covers general advice for using the accompanying rich text renderer for Android.

Rendering times
===============

Sometimes the rendering of native Android views can get very time consuming, especially if the nesting level of the rich
text nodes is high. For this reason it is advised to take a look at `RemoveToDeepNesting`-simplifier, since this
simplifier will remove all elements below a given nesting level.

Rendering Embedded / Hyperlinks
===============================

Every custom visualization needs to be defined by the client. If a client needs a hyperlink or embedded link to an
Entry or to an Asset, they would need to add the renderer as described above. Sadly Contentful cannot know their use
case especially the build of an entries fields cannot be predicted (Which field to use for the description? Anything
important to be displayed, etc.) This library does require the help of customers to render custom material.
