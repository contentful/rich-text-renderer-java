package com.contentful.rich.android.sample

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.contentful.rich.android.AndroidContext
import com.contentful.rich.android.AndroidProcessor
import kotlinx.android.synthetic.main.fragment_page.*
import kotlinx.android.synthetic.main.fragment_page.view.*
import kotlinx.android.synthetic.main.sample_item.view.*

class PageFragment : Fragment() {
    class Holder(view: View) : RecyclerView.ViewHolder(view)

    class CharSequenceAdapter(pageIndex: Int, private val androidContext: Context) : RecyclerView.Adapter<Holder>() {
        private val page: Page = PAGES[pageIndex]
        private val inflater: LayoutInflater = LayoutInflater.from(androidContext)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
                Holder(inflater.inflate(R.layout.sample_item, parent, false))

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val item = page.document.content[position]

            val processor = AndroidProcessor.creatingCharSequences()
            val context = AndroidContext(androidContext)
            val text = processor.process(context, item) ?: "???"

            holder.itemView.sample_item_text.text = text
            holder.itemView.sample_item_text.movementMethod = LinkMovementMethod.getInstance()
        }

        override fun getItemCount(): Int = page.document.content.size
    }

    class NativeViewAdapter(pageIndex: Int, private val androidContext: Context) : RecyclerView.Adapter<Holder>() {
        private val page: Page = PAGES[pageIndex]
        private val inflater: LayoutInflater = LayoutInflater.from(androidContext)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
                Holder(inflater.inflate(R.layout.sample_item, parent, false))

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val item = page.document.content[position]

            val processor = AndroidProcessor.creatingNativeViews()
            val context = AndroidContext(androidContext)
            val view = processor.process(context, item)
                    ?: TextView(this.androidContext).apply { setText(R.string.error_no_view) }

            holder.itemView.sample_item_card.removeAllViews()
            holder.itemView.sample_item_card.addView(view)
        }

        override fun getItemCount(): Int = page.document.content.size

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_page, container, false)
        rootView.page_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rootView.page_recycler.adapter =
                CharSequenceAdapter(arguments?.getInt(ARG_PAGE_INDEX, 0) ?: 0, context!!)

        rootView.main_native_check.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onNativeViewProcessingSelected()
            } else {
                onCharSequenceProcessingSelected()
            }
        }

        return rootView
    }

    private fun onNativeViewProcessingSelected() {
        fragment_page.page_recycler.adapter =
                NativeViewAdapter(arguments?.getInt(ARG_PAGE_INDEX, 0) ?: 0, context!!)

    }

    private fun onCharSequenceProcessingSelected() {
        fragment_page.page_recycler.adapter =
                CharSequenceAdapter(arguments?.getInt(ARG_PAGE_INDEX, 0) ?: 0, context!!)
    }

    companion object {
        private const val ARG_PAGE_INDEX = "page_index"

        fun newInstance(pageIndex: Int): PageFragment {
            val fragment = PageFragment()
            val args = Bundle()
            args.putInt(ARG_PAGE_INDEX, pageIndex)
            fragment.arguments = args
            return fragment
        }
    }
}