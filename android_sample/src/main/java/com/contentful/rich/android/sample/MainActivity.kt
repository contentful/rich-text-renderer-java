package com.contentful.rich.android.sample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.core.view.WindowInsetsControllerCompat
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
private const val TOKEN = "yx3Ub1EQ6KVk7b4IATe5s7l5q2F5pdYZvCfkEiyuSNQ"
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
        
        // Set toolbar text color for better visibility
        binding.toolbar.setTitleTextColor(android.graphics.Color.WHITE)
        
        // Set spinner text color for better visibility
        binding.spinner.setPopupBackgroundResource(android.R.color.white)

        // Handle window insets to prevent status bar overlap
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarLayout) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }

        // Configure status bar appearance for proper contrast
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Use system theme to determine status bar icon color
        val isDarkTheme = (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
        windowInsetsController.isAppearanceLightStatusBars = !isDarkTheme

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
                    if(entry.getField<Any>("en", "rich") != null) {
                        entry.getField<Any>("en", "rich") as CDARichDocument
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