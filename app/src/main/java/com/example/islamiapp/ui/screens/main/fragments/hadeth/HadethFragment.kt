package com.example.islamiapp.ui.screens.main.fragments.hadeth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.islamiapp.R
import com.example.islamiapp.model.HadethDm

class HadethFragment : Fragment() {
    var ahadeth: MutableList<HadethDm> = emptyList<HadethDm>().toMutableList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hadeth, container, false)
    }

    fun readAhadethFile() {
        val inputStream = requireActivity().assets.open("ahadeth.txt")
        var title = ""
        var content = ""
        inputStream.reader().forEachLine {
            if (title.isEmpty()) {
                title = it
            } else {
                if (it.equals("#")) {
                    ahadeth.add(HadethDm(title = title, content = content))
                    title = ""
                    content = ""
                    return@forEachLine
                }
                content += it
            }
        }
        ahadeth.add(HadethDm(title = title, content = content))
    }
}