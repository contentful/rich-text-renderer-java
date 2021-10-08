package com.contentful.rich.android.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_page.*
import kotlinx.android.synthetic.main.fragment_page.view.*

class PageFragment : Fragment() {
    class Holder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_page, container, false)
        rootView.page_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rootView.page_recycler.adapter =
                RichCharSequenceAdapter(arguments?.getInt(ARG_PAGE_INDEX, 0)
                        ?: 0, requireContext())

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
                RichNativeViewAdapter(
                        arguments?.getInt(ARG_PAGE_INDEX, 0) ?: 0,
                        requireContext())

    }

    private fun onCharSequenceProcessingSelected() {
        fragment_page.page_recycler.adapter =
                RichCharSequenceAdapter(
                        arguments?.getInt(ARG_PAGE_INDEX, 0) ?: 0,
                        requireContext())
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