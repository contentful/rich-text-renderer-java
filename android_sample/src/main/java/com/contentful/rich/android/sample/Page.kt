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
                        )
                )
        )
)

fun <T : CDARichBlock> T.addAll(vararg elements: CDARichNode): T {
    elements.forEach { this.content.add(it) }
    return this
}