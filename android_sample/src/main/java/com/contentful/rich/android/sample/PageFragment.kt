package com.contentful.rich.android.sample

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.contentful.rich.android.sample.databinding.FragmentPageBinding
import com.contentful.rich.android.sample.databinding.SampleItemBinding

class PageFragment : Fragment() {
    class Holder(private val binding: SampleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.sampleItemText.text = text
            binding.sampleItemText.movementMethod = LinkMovementMethod.getInstance()
        }
        fun removeViews(view: View) {
            binding.sampleItemCard.removeAllViews()
            binding.sampleItemCard.addView(view)
        }
    }

    private var _binding: FragmentPageBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPageBinding.inflate(inflater, container, false)
        binding.pageRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.pageRecycler.adapter =
                RichCharSequenceAdapter(arguments?.getInt(ARG_PAGE_INDEX, 0)
                        ?: 0, requireContext())

        binding.mainNativeCheck.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onNativeViewProcessingSelected()
            } else {
                onCharSequenceProcessingSelected()
            }
        }

        return binding.root
    }

    private fun onNativeViewProcessingSelected() {
        binding.pageRecycler.adapter =
                RichNativeViewAdapter(
                        arguments?.getInt(ARG_PAGE_INDEX, 0) ?: 0,
                        requireContext())

    }

    private fun onCharSequenceProcessingSelected() {
        binding.pageRecycler.adapter =
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