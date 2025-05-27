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
import com.contentful.rich.android.sample.databinding.ActivityMainBinding
import com.contentful.rich.core.simple.RemoveEmpties
import com.contentful.rich.core.simple.RemoveToDeepNesting
import com.contentful.rich.core.simple.Simplifier
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val SPACE_ID = "i1ppeoxgdpvt"
private const val TOKEN = "ec6NU4tHJupJ_SLf-sAq5QRDbJvJkRcBTMnha9XFYuI"
private const val ENVIRONMENT = "master"

class MainActivity(
        private val client: CDAClient = CDAClient.builder()
                .setSpace(SPACE_ID)
                .setToken(TOKEN)
                .setEnvironment(ENVIRONMENT)
                .build()
) : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (PAGES.last().name != "Contentful") {
            binding.spinner.prompt = "⌛ Loading from Contentful ⏳"
            loadPageFromContentful()
        }

        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
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
                        if(entry.getField<Any>("rich") != null) {
                            entry.getField<Any>("rich") as CDARichDocument
                        } else {
                            CDARichDocument()
                        }
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
                binding.spinner.prompt = PAGES[0].name
                binding.spinner.adapter = PageAdapter(
                        this@MainActivity,
                        PAGES.toTypedArray()
                )
            }
        }
    }
}
