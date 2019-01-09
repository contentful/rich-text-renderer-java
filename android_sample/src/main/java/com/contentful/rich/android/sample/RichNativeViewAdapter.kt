package com.contentful.rich.android.sample

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.rich.CDARichHyperLink
import com.contentful.rich.android.AndroidContext
import com.contentful.rich.android.AndroidProcessor
import kotlinx.android.synthetic.main.sample_item.view.*

class RichNativeViewAdapter(pageIndex: Int, private val androidContext: Context) : RecyclerView.Adapter<PageFragment.Holder>() {
    private val page: Page = PAGES[pageIndex]
    private val inflater: LayoutInflater = LayoutInflater.from(androidContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageFragment.Holder =
            PageFragment.Holder(inflater.inflate(R.layout.sample_item, parent, false))

    override fun onBindViewHolder(holder: PageFragment.Holder, position: Int) {
        val item = page.document.content[position]
        val context = AndroidContext(androidContext)

        val processor = AndroidProcessor.creatingNativeViews()
        processor.overrideRenderer(
                { _, node -> node is CDARichHyperLink && node.data is CDAEntry },
                { _, node ->
                    val data = (node as CDARichHyperLink).data as CDAEntry
                    TextView(androidContext).apply {
                        text = resources.getString(R.string.hyperlink_entry_inline, data.id())
                        setOnClickListener {
                            androidContext.toast(resources.getString(R.string.hyperlink_entry_inline_clicked, data.id()))
                        }
                    }
                }
        )

        processor.overrideRenderer(
                { _, node -> node is CDARichHyperLink && node.data is CDAAsset },
                { _, node ->
                    val data = (node as CDARichHyperLink).data as CDAAsset
                    TextView(androidContext).apply {
                        text = resources.getString(R.string.hyperlink_asset_inline, data.id())
                        setOnClickListener {
                            androidContext.toast(resources.getString(R.string.hyperlink_asset_inline_clicked, data.id()))
                        }
                    }
                }
        )

        val view = processor.process(context, item)
                ?: TextView(this.androidContext).apply { setText(R.string.error_no_view) }

        holder.itemView.sample_item_card.removeAllViews()
        holder.itemView.sample_item_card.addView(view)
    }

    override fun getItemCount(): Int = page.document.content.size

}

fun Context.toast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
