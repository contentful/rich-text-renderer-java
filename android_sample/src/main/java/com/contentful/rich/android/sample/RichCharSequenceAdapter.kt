package com.contentful.rich.android.sample

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.rich.CDARichHyperLink
import com.contentful.rich.android.AndroidContext
import com.contentful.rich.android.AndroidProcessor
import com.contentful.rich.android.sample.databinding.SampleItemBinding

class RichCharSequenceAdapter(pageIndex: Int, private val androidContext: Context) : RecyclerView.Adapter<PageFragment.Holder>() {
    private val page: Page = PAGES[pageIndex]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageFragment.Holder {
        val itemBinding = SampleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PageFragment.Holder(itemBinding)
    }
    override fun onBindViewHolder(holder: PageFragment.Holder, position: Int) {
        val item = page.document.content[position]
        val context = AndroidContext(androidContext)
        val processor = AndroidProcessor.creatingCharSequences()
        processor.overrideRenderer(
                { _, node -> node is CDARichHyperLink && node.data is CDAEntry },
                { _, node ->
                    val data = (node as CDARichHyperLink).data as CDAEntry
                    val text = androidContext.resources.getString(R.string.hyperlink_entry_inline, data.id())
                    val message = androidContext.resources.getString(R.string.hyperlink_entry_inline_clicked, data.id())

                    SpannableStringBuilder(text).apply {
                        setSpan(
                                object : ClickableSpan() {
                                    override fun onClick(widget: View) {
                                        androidContext.toast(message)
                                    }
                                }, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }.toString()
                }
        )

        processor.overrideRenderer(
                { _, node -> node is CDARichHyperLink && node.data is CDAAsset },
                { _, node ->
                    val data = (node as CDARichHyperLink).data as CDAAsset
                    val text = androidContext.resources.getString(R.string.hyperlink_asset_inline, data.id())
                    val message = androidContext.resources.getString(R.string.hyperlink_asset_inline_clicked, data.id())

                    SpannableStringBuilder(text).apply {
                        setSpan(
                                object : ClickableSpan() {
                                    override fun onClick(widget: View) {
                                        androidContext.toast(message)
                                    }
                                }, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }.toString()
                }
        )

        val text = processor.process(context, item) ?: "???"
        holder.bind(text.toString())
    }

    override fun getItemCount(): Int = page.document.content.size
}