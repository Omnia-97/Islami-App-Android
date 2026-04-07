package com.example.islamiapp.ui.screens.sura_details

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.islamiapp.R
import com.example.islamiapp.model.SuraDM

class SuraDetailsActivity : AppCompatActivity() {
    lateinit var sura: SuraDM
    lateinit var suraNameAr: TextView
    lateinit var suraNameEn: TextView
    lateinit var backArrow: ImageView
    lateinit var suraContentTextView : TextView


    companion object {
        const val SURA_KEY = "sura"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sura_details)
        initViews()
        initListeners()
        readSuraFile()

    }

    private fun initViews() {
        sura = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(SURA_KEY, SuraDM::class.java)!!
        } else {
            intent.getSerializableExtra(SURA_KEY) as SuraDM
        }
        suraNameAr = findViewById(R.id.suraNameAr)
        suraNameEn = findViewById(R.id.suraNameEn)
        backArrow = findViewById(R.id.icBackArrow)
        suraContentTextView = findViewById(R.id.suraContent)
        suraNameAr.text = sura.nameAr
        suraNameEn.text = sura.nameEn
    }

    private fun initListeners() {
        backArrow.setOnClickListener {
            finish()
        }
    }

    private fun readSuraFile() {
        val fileName = "${sura.index}.txt"
        val inputStream = assets.open("Suras/$fileName")
        var suraContent = ""
        var i =1;
        inputStream.reader().forEachLine { line->
            if (line.isNotEmpty()){
                suraContent += "$line {$i} "
                i++
            }
        }
        suraContentTextView.text = suraContent
    }
}