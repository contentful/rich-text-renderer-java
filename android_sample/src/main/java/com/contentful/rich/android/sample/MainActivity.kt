package com.contentful.rich.android.sample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        spinner.adapter = PageAdapter(
                toolbar.context,
                PAGES
        )

        spinner.onItemSelectedListener =
                object : OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.container, PageFragment.newInstance(position))
                                .commit()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }

        spinner.prompt = PAGES[0].name
    }
}
