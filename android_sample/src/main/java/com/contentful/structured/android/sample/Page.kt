package com.contentful.structured.android.sample

import com.contentful.java.cda.structured.*
import com.contentful.java.cda.structured.CDAStructuredMark.*

data class Page(val name: String, val document: CDAStructuredDocument)

val PAGES: Array<Page> = arrayOf(
        Page(name = "Text with Marks",
                document = CDAStructuredDocument().addAll(
                        CDAStructuredText(listOf(), "Normal Text"),
                        CDAStructuredText(listOf(CDAStructuredMarkBold()), "BoldText"),
                        CDAStructuredText(listOf(CDAStructuredMarkItalic()), "Italic"),
                        CDAStructuredText(listOf(CDAStructuredMarkUnderline()), "Underline"),
                        CDAStructuredText(listOf(CDAStructuredMarkCode()), "final String code;"),
                        CDAStructuredText(listOf(CDAStructuredMarkCustom("\uD83E\uDD16")), "CustomText"),
                        CDAStructuredText(
                                listOf(
                                        CDAStructuredMarkCustom("custom"),
                                        CDAStructuredMarkItalic(),
                                        CDAStructuredMarkBold(),
                                        CDAStructuredMarkCode(),
                                        CDAStructuredMarkUnderline()
                                ),
                                "All in all"
                        )
                )
        ),
        Page(name = "Headings",
                document = CDAStructuredDocument().addAll(
                        *(1 until 7).map {
                            CDAStructuredHeading(it).addAll(CDAStructuredText(listOf(), "Heading level $it"))
                        }.toTypedArray()
                )
        ),
        Page(name = "Lists",
                document = CDAStructuredDocument().addAll(
                        CDAStructuredOrderedList().addAll(
                                CDAStructuredListItem().addAll(
                                        CDAStructuredText(listOf(), "first item")
                                ),
                                CDAStructuredListItem().addAll(
                                        CDAStructuredText(listOf(), "second item")
                                ),
                                CDAStructuredListItem().addAll(
                                        CDAStructuredText(listOf(), "third item")
                                )
                        ),
                        CDAStructuredUnorderedList().addAll(
                                CDAStructuredListItem().addAll(
                                        CDAStructuredText(listOf(), "first item")
                                ),
                                CDAStructuredListItem().addAll(
                                        CDAStructuredText(listOf(), "second item")
                                ),
                                CDAStructuredListItem().addAll(
                                        CDAStructuredText(listOf(), "third item")
                                )
                        ),
                        CDAStructuredUnorderedList().addAll(
                                CDAStructuredListItem().addAll(
                                        CDAStructuredText(listOf(), "first item")
                                ),
                                CDAStructuredListItem().addAll(
                                        CDAStructuredText(listOf(), "second item"),
                                        CDAStructuredOrderedList().addAll(
                                                CDAStructuredListItem().addAll(
                                                        CDAStructuredText(listOf(), "first nested item")
                                                ),
                                                CDAStructuredListItem().addAll(
                                                        CDAStructuredText(listOf(), "second nested item")
                                                ),
                                                CDAStructuredListItem().addAll(
                                                        CDAStructuredText(listOf(), "third nested item")
                                                )
                                        )
                                ),
                                CDAStructuredListItem().addAll(
                                        CDAStructuredText(listOf(), "third item")
                                )
                        )
                )
        )
)

