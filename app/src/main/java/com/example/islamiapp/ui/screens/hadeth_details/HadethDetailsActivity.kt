package com.example.islamiapp.ui.screens.hadeth_details

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.islamiapp.R
import com.example.islamiapp.model.HadethDm

class HadethDetailsActivity : AppCompatActivity() {
    lateinit var hadethDm: HadethDm
    lateinit var suraNameAr: TextView
    lateinit var suraNameEn: TextView
    lateinit var backArrow: ImageView
    lateinit var suraContentTextView: TextView

    companion object {
        const val HADETH_KEY = "hadeth"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hadeth_details)
        initViews()
        initListeners()
    }

    private fun initViews() {
        hadethDm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(HADETH_KEY, HadethDm::class.java)!!
        } else {
            intent.getSerializableExtra(HADETH_KEY) as HadethDm
        }
        suraNameAr = findViewById(R.id.suraNameAr)
        suraNameEn = findViewById(R.id.suraNameEn)
        backArrow = findViewById(R.id.icBackArrow)
        suraContentTextView = findViewById(R.id.suraContent)
        suraNameAr.text = hadethDm.title
        suraContentTextView.text = hadethDm.content
    }

    private fun initListeners() {
        backArrow.setOnClickListener {
            finish()
        }
    }
}