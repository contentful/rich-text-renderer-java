package com.contentful.structured.android.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.contentful.java.cda.structured.CDAStructuredDocument
import com.contentful.java.cda.structured.CDAStructuredMark.*
import com.contentful.java.cda.structured.CDAStructuredNode
import com.contentful.java.cda.structured.CDAStructuredParagraph
import com.contentful.java.cda.structured.CDAStructuredText
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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

fun <T : CDAStructuredParagraph> T.addAll(vararg elements: CDAStructuredNode): T {
    elements.forEach { this.content.add(it) }
    return this
}