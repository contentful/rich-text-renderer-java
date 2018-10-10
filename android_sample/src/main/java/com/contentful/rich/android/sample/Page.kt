package com.contentful.rich.android.sample

import com.contentful.java.cda.rich.*
import com.contentful.java.cda.rich.CDARichMark.*

data class Page(val name: String, val document: CDARichDocument)

val PAGES: Array<Page> = arrayOf(
        Page(name = "Text with Marks",
                document = CDARichDocument().addAll(
                        CDARichText("Normal Text"),
                        CDARichText("BoldText", listOf(CDARichMarkBold())),
                        CDARichText("Italic", listOf(CDARichMarkItalic())),
                        CDARichText("Underline", listOf(CDARichMarkUnderline())),
                        CDARichText("final String code;", listOf(CDARichMarkCode())),
                        CDARichText("CustomText", listOf(CDARichMarkCustom("custom"))),
                        CDARichText("All in all",
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
                            CDARichHeading(it).addAll(CDARichText("Heading level $it"))
                        }.toTypedArray()
                )
        ),
        Page(name = "Lists",
                document = CDARichDocument().addAll(
                        CDARichOrderedList().addAll(
                                CDARichListItem().addAll(CDARichText("first item")),
                                CDARichListItem().addAll(CDARichText("second item")),
                                CDARichListItem().addAll(CDARichText("third item"))
                        ),
                        CDARichUnorderedList().addAll(
                                CDARichListItem().addAll(CDARichText("first item")),
                                CDARichListItem().addAll(CDARichText("second item")),
                                CDARichListItem().addAll(CDARichText("third item"))
                        ),
                        CDARichUnorderedList().addAll(
                                CDARichListItem().addAll(CDARichText("first item")),
                                CDARichListItem().addAll(
                                        CDARichText("second item"),
                                        CDARichOrderedList().addAll(
                                                CDARichListItem().addAll(CDARichText("first nested item")),
                                                CDARichListItem().addAll(CDARichText("second nested item")),
                                                CDARichListItem().addAll(CDARichText("third nested item"))
                                        )
                                ),
                                CDARichListItem().addAll(CDARichText("third item"))
                        ),
                        CDARichOrderedList().addAll(
                                CDARichListItem().addAll(CDARichText("first item")),
                                CDARichListItem().addAll(
                                        CDARichText("second item"),
                                        CDARichUnorderedList().addAll(
                                                CDARichListItem().addAll(CDARichText("first nested item")),
                                                CDARichListItem().addAll(CDARichText("second nested item")),
                                                CDARichListItem().addAll(CDARichText("third nested item"))
                                        )
                                ),
                                CDARichListItem().addAll(CDARichText("third item"))
                        ),
                        CDARichList("1").addAll(
                                CDARichListItem().addAll(CDARichText("one")),
                                CDARichList("A").addAll(
                                        CDARichListItem().addAll(CDARichText("two")),
                                        CDARichList("a").addAll(
                                                CDARichListItem().addAll(CDARichText("three")),
                                                CDARichList("I").addAll(
                                                        CDARichListItem().addAll(CDARichText("four"))
                                                )
                                        )
                                )
                        ),
                        CDARichUnorderedList().addAll(
                                CDARichListItem().addAll(CDARichText("one")),
                                CDARichUnorderedList().addAll(
                                        CDARichListItem().addAll(CDARichText("two")),
                                        CDARichUnorderedList().addAll(
                                                CDARichListItem().addAll(CDARichText("three")),
                                                CDARichUnorderedList().addAll(
                                                        CDARichListItem().addAll(CDARichText("four"))
                                                )
                                        )
                                )
                        )
                )
        ),
        Page(name = "Quotes",
                document = CDARichDocument().addAll(
                        CDARichBlock().addAll(
                                CDARichText("Quotes From "),
                                CDARichText("Johann Wolfgang von Goethe", listOf(CDARichMark.CDARichMarkBold())),
                                CDARichText(": Faust", listOf(CDARichMark.CDARichMarkItalic()))
                        ),
                        CDARichQuote().addAll(
                                CDARichText("Ihr naht euch wieder, schwankende Gestalten,\n" +
                                        "Die früh sich einst dem trüben Blick gezeigt.\n" +
                                        "Versuch ich wohl, euch diesmal festzuhalten?\n" +
                                        "Fühl ich mein Herz noch jenem Wahn geneigt?\n" +
                                        "Ihr drängt euch zu! nun gut, so mögt ihr walten,\n" +
                                        "Wie ihr aus Dunst und Nebel um mich steigt;\n" +
                                        "Mein Busen fühlt sich jugendlich erschüttert\n" +
                                        "Vom Zauberhauch, der euren Zug umwittert.", listOf(CDARichMark.CDARichMarkCode())
                                ),
                                CDARichText("Ihr bringt mit euch die Bilder froher Tage,\n" +
                                        "Und manche liebe Schatten steigen auf;\n" +
                                        "Gleich einer alten, halbverklungnen Sage\n" +
                                        "Kommt erste Lieb und Freundschaft mit herauf;\n" +
                                        "Der Schmerz wird neu, es wiederholt die Klage\n" +
                                        "Des Lebens labyrinthisch irren Lauf,\n" +
                                        "Und nennt die Guten, die, um schöne Stunden\n" +
                                        "Vom Glück getäuscht, vor mir hinweggeschwunden."
                                        , listOf(CDARichMark.CDARichMarkCode())
                                )
                        )
                )

        ),
        Page(name = "Links", document = CDARichDocument().addAll(
                CDARichHyperLink("https://www.google.com/search?hl=en&site=imghp&tbm=isch&source=hp&q=cats")
                        .addAll(
                                CDARichText("Hyperlink to cats.")
                        )
        )
        )
)


fun <T : CDARichBlock> T.addAll(vararg elements: CDARichNode): T {
    elements.forEach { this.content.add(it) }
    return this
}