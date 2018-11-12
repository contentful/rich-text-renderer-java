package com.contentful.rich.android.sample

import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.CDAResource
import com.contentful.java.cda.LocalizedResource
import com.contentful.java.cda.rich.*
import com.contentful.java.cda.rich.CDARichMark.*
import java.lang.reflect.Field

data class Page(val name: String, val document: CDARichDocument)

val PAGES: Array<Page> = arrayOf(
        Page(name = "Text with Marks",
                document = CDARichDocument().addAll(
                        text("Normal Text"),
                        text("BoldText", listOf(CDARichMarkBold())),
                        text("Italic", listOf(CDARichMarkItalic())),
                        text("Underline", listOf(CDARichMarkUnderline())),
                        text("final String code;", listOf(CDARichMarkCode())),
                        text("CustomText", listOf(CDARichMarkCustom("custom"))),
                        CDARichHorizontalRule(),
                        text("All in all",
                                listOf(
                                        CDARichMarkCustom("custom"),
                                        CDARichMarkItalic(),
                                        CDARichMarkBold(),
                                        CDARichMarkCode(),
                                        CDARichMarkUnderline()
                                )
                        )
                )
        ),
        Page(name = "Headings",
                document = CDARichDocument().addAll(
                        *(1 until 7).map {
                            CDARichHeading(it).addAll(text("Heading level $it"))
                        }.toTypedArray()
                )
        ),
        Page(name = "Lists",
                document = CDARichDocument().addAll(
                        CDARichOrderedList().addAll(
                                CDARichListItem().addAll(text("first item")),
                                CDARichListItem().addAll(text("second item")),
                                CDARichListItem().addAll(text("third item"))
                        ),
                        CDARichUnorderedList().addAll(
                                CDARichListItem().addAll(text("first item")),
                                CDARichListItem().addAll(text("second item")),
                                CDARichListItem().addAll(text("third item"))
                        ),
                        CDARichUnorderedList().addAll(
                                CDARichListItem().addAll(text("first item")),
                                CDARichListItem().addAll(
                                        text("second item"),
                                        CDARichOrderedList().addAll(
                                                CDARichListItem().addAll(text("first nested item")),
                                                CDARichListItem().addAll(text("second nested item")),
                                                CDARichListItem().addAll(text("third nested item"))
                                        )
                                ),
                                CDARichListItem().addAll(text("third item"))
                        ),
                        CDARichOrderedList().addAll(
                                CDARichListItem().addAll(text("first item")),
                                CDARichListItem().addAll(
                                        text("second item"),
                                        CDARichUnorderedList().addAll(
                                                CDARichListItem().addAll(text("first nested item")),
                                                CDARichListItem().addAll(text("second nested item")),
                                                CDARichListItem().addAll(text("third nested item"))
                                        )
                                ),
                                CDARichListItem().addAll(text("third item"))
                        ),
                        CDARichOrderedList().addAll(
                                CDARichListItem().addAll(text("first item")),
                                CDARichListItem().addAll(
                                        text("second item"),
                                        CDARichOrderedList().addAll(
                                                CDARichListItem().addAll(text("first nested item")),
                                                CDARichListItem().addAll(text("second nested item")),
                                                CDARichListItem().addAll(text("third nested item"))
                                        )
                                ),
                                CDARichListItem().addAll(text("third item"))
                        ),
                        CDARichUnorderedList().addAll(
                                CDARichListItem().addAll(text("first item")),
                                CDARichListItem().addAll(
                                        text("second item"),
                                        CDARichOrderedList().addAll(
                                                CDARichListItem().addAll(text("first nested item")),
                                                CDARichListItem().addAll(text("second nested item")),
                                                CDARichListItem().addAll(text("third nested item"))
                                        )
                                ),
                                CDARichListItem().addAll(text("third item"))
                        ),
                        CDARichList("1").addAll(
                                CDARichListItem().addAll(text("one")),
                                CDARichList("A").addAll(
                                        CDARichListItem().addAll(text("two")),
                                        CDARichList("a").addAll(
                                                CDARichListItem().addAll(text("three")),
                                                CDARichList("I").addAll(
                                                        CDARichListItem().addAll(text("four"))
                                                )
                                        )
                                )
                        ),
                        CDARichUnorderedList().addAll(
                                CDARichListItem().addAll(text("one")),
                                CDARichUnorderedList().addAll(
                                        CDARichListItem().addAll(text("two")),
                                        CDARichUnorderedList().addAll(
                                                CDARichListItem().addAll(text("three")),
                                                CDARichUnorderedList().addAll(
                                                        CDARichListItem().addAll(text("four"))
                                                )
                                        )
                                )
                        )
                )
        ),
        Page(name = "Quotes",
                document = CDARichDocument().addAll(
                        CDARichBlock().addAll(
                                text("Quotes from "),
                                text("Famous", listOf(CDARichMark.CDARichMarkBold())),
                                text(" People", listOf(CDARichMark.CDARichMarkItalic()))
                        ),
                        CDARichQuote().addAll(
                                text("You know you’re in love when you can’t fall asleep because reality is finally better than your dreams."),
                                text("- Dr. Suess", listOf(CDARichMark.CDARichMarkItalic()))
                        ),
                        CDARichQuote().addAll(
                                text("I’m selfish, impatient and a little insecure. I make mistakes, I am out of control and at times hard to handle. But if you can’t handle me at my worst, then you sure as hell don’t deserve me at my best."),
                                text("- Marilyn Monroe", listOf(CDARichMark.CDARichMarkItalic()))
                        ),
                        CDARichQuote().addAll(
                                text("Twenty years from now you will be more disappointed by the things that you didn’t do than by the ones you did do."),
                                text("- Mark Twain", listOf(CDARichMark.CDARichMarkItalic()))
                        )
                )

        ),
        Page(name = "Links", document = CDARichDocument().addAll(
                CDARichHyperLink("https://www.google.com/search?hl=en&site=imghp&tbm=isch&source=hp&q=cats")
                        .addAll(text("Hyperlink to cats")),
                CDARichEmbeddedLink(mockCDAEntry())
                        .addAll(text(" Embedded entry")),
                CDARichEmbeddedLink(mockCDAAsset())
                        .addAll(text(" Embedded asset"))
        )
        )
)

fun text(name: String, marks:List<CDARichMark> = listOf<CDARichMark>()) = CDARichText(name, marks)

fun <T : CDARichBlock> T.addAll(vararg elements: CDARichNode): T {
    elements.forEach { this.content.add(it) }
    return this
}

fun mockCDAEntry(): CDAEntry {
    val entry = CDAEntry()
    mockSysAndFields(entry)

    entry.attrs()["id"] = "fake_id"
    entry.attrs()["contentType"] = "fake_type"

    return entry
}

fun mockCDAAsset(): CDAAsset {
    val asset = CDAAsset()
    mockSysAndFields(asset)

    asset.attrs()["id"] = "fake_id"
    asset.setField("en-US", "file", mapOf(
            "url" to "https://www.contentful.com/assets/images/favicons/favicon.png",
            "contentType" to "image/png"
    ))

    return asset
}

private fun mockSysAndFields(resource: CDAResource) {
    var attrs: Field? = null
    for (field in CDAResource::class.java.declaredFields) {
        if ("attrs" == field.name) {
            attrs = field
        }
    }

    var fields: Field? = null
    var defaultLocale: Field? = null
    for (field in LocalizedResource::class.java.declaredFields) {
        if ("fields" == field.name) {
            fields = field
        } else if ("defaultLocale" == field.name) {
            defaultLocale = field
        }
    }

    if (attrs != null) {
        attrs.isAccessible = true
        try {
            attrs.set(resource, hashMapOf<Any?, Any?>())
        } catch (e: IllegalAccessException) {
        }

        attrs.isAccessible = false
    }

    if (fields != null) {
        fields.isAccessible = true
        try {
            val localized = hashMapOf<Any?, Any?>()
            localized["title"] = mutableMapOf("en-US" to "Title")
            localized["file"] = mutableMapOf("en-US" to mutableMapOf<Any?, Any?>())
            fields.set(resource, localized)
        } catch (e: IllegalAccessException) {
        }

        fields.isAccessible = false
    }

    if (defaultLocale != null) {
        defaultLocale.isAccessible = true
        try {
            defaultLocale.set(resource, "en-US")
        } catch (e: IllegalAccessException) {
        }

        defaultLocale.isAccessible = false
    }
}
