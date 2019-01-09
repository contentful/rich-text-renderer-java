package com.contentful.rich.android.sample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.contentful.java.cda.CDAClient
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.QueryOperation.Matches
import com.contentful.java.cda.rich.CDARichDocument
import com.contentful.java.cda.rich.CDARichParagraph
import com.contentful.rich.core.simple.RemoveEmpties
import com.contentful.rich.core.simple.RemoveToDeepNesting
import com.contentful.rich.core.simple.Simplifier
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity(
        private val client: CDAClient = CDAClient.builder()
                .setSpace(BuildConfig.SPACE_ID)
                .setToken(BuildConfig.DELIVERY_TOKEN)
                .setEnvironment(BuildConfig.ENVIRONMENT_ID)
                .build()
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (PAGES.last().name != "Contentful") {
            spinner.prompt = "⌛ Loading from Contentful ⏳"
            loadPageFromContentful()
        }

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, PageFragment.newInstance(position))
                        .commit()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadPageFromContentful() {
        GlobalScope.launch {
            val document = client
                    .fetch(CDAEntry::class.java)
                    .withContentType("rich")
                    .where("fields.name", Matches, "simple")
                    .orderBy("fields.name")
                    .all()
                    .entries()
                    .values
                    .map { entry ->
                        entry.getField<Any>("rich") as CDARichDocument
                    }.reduce { a, b ->
                        val p = CDARichParagraph()
                        p.content.addAll(b.content)
                        a.content.add(p)
                        a
                    }

            PAGES.add(
                    Page("Contentful",
                            Simplifier(
                                    RemoveToDeepNesting(10),
                                    RemoveEmpties()
                            ).simplify(document) as CDARichDocument)
            )

            runOnUiThread {
                spinner.prompt = PAGES[0].name
                spinner.adapter = PageAdapter(
                        this@MainActivity,
                        PAGES.toTypedArray()
                )
            }
        }
    }
}
