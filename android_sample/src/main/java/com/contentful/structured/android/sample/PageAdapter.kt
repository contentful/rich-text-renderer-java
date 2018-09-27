package com.contentful.structured.android.sample

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ThemedSpinnerAdapter
import kotlinx.android.synthetic.main.dropdown_item.view.*

class PageAdapter(context: Context, objects: Array<Page>) :
        ArrayAdapter<Page>(context, R.layout.appbar_item, objects), ThemedSpinnerAdapter {

    private val mDropDownHelper: ThemedSpinnerAdapter.Helper = ThemedSpinnerAdapter.Helper(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null) {
            val inflater = mDropDownHelper.dropDownViewInflater
            inflater.inflate(R.layout.appbar_item, parent, false)
        } else {
            convertView
        }

        view.list_item_text.text = getItem(position)?.name ?: "null"

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null) {
            val inflater = mDropDownHelper.dropDownViewInflater
            inflater.inflate(R.layout.dropdown_item, parent, false)
        } else {
            convertView
        }

        view.list_item_text.text = getItem(position)?.name ?: "null"

        return view
    }

    override fun getDropDownViewTheme(): Resources.Theme? {
        return mDropDownHelper.dropDownViewTheme
    }

    override fun setDropDownViewTheme(theme: Resources.Theme?) {
        mDropDownHelper.dropDownViewTheme = theme
    }
}