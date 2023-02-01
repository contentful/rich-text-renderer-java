package com.contentful.rich.android.sample

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ThemedSpinnerAdapter
import com.contentful.rich.android.sample.databinding.AppbarItemBinding
import com.contentful.rich.android.sample.databinding.DropdownItemBinding

class PageAdapter(context: Context, objects: Array<Page>) :
        ArrayAdapter<Page>(context, R.layout.appbar_item, objects), ThemedSpinnerAdapter {

    private val mDropDownHelper: ThemedSpinnerAdapter.Helper = ThemedSpinnerAdapter.Helper(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: AppbarItemBinding =
            if (convertView != null) AppbarItemBinding.bind(convertView)
            else AppbarItemBinding.inflate(LayoutInflater.from(context), parent, false)

        binding.listItemText.text = getItem(position)?.name ?: "null"

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: DropdownItemBinding =
                if (convertView != null) DropdownItemBinding.bind(convertView)
                else DropdownItemBinding.inflate(LayoutInflater.from(context), parent, false)

        binding.listItemText.text = getItem(position)?.name ?: "null"

        return binding.root
    }

    override fun getDropDownViewTheme(): Resources.Theme? {
        return mDropDownHelper.dropDownViewTheme
    }

    override fun setDropDownViewTheme(theme: Resources.Theme?) {
        mDropDownHelper.dropDownViewTheme = theme
    }
}