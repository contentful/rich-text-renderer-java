package com.contentful.rich.android.sample

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.contentful.rich.android.AndroidContext
import com.contentful.rich.android.AndroidProcessor
import kotlinx.android.synthetic.main.fragment_page.view.*
import kotlinx.android.synthetic.main.sample_item.view.*

class PageFragment : Fragment() {
    class Holder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_page, container, false)
        val pageIndex = arguments?.getInt(ARG_PAGE_INDEX, 0) ?: 0
        val page = PAGES[pageIndex]

        rootView.page_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rootView.page_recycler.adapter = object : RecyclerView.Adapter<Holder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
                    Holder(inflater.inflate(R.layout.sample_item, parent, false))

            override fun onBindViewHolder(holder: Holder, position: Int) {
                val item = page.document.content[position]

                val processor = AndroidProcessor.creatingCharSequences()
                val context = AndroidContext(context)
                val text = processor.process(context, item) ?: "???"

                holder.itemView.sample_item_text.text = text
                holder.itemView.sample_item_text.movementMethod = LinkMovementMethod.getInstance()
            }

            override fun getItemCount(): Int = page.document.content.size
        }

        return rootView
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