package com.example.islamiapp.ui.screens.main.fragments.hadeth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.islamiapp.R
import com.example.islamiapp.model.HadethDm
import com.example.islamiapp.ui.screens.hadeth_details.HadethDetailsActivity
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy

class HadethFragment : Fragment() {
    var ahadeth: MutableList<HadethDm> = emptyList<HadethDm>().toMutableList()
    lateinit var hadethAdapter: HadethAdapter
    lateinit var hadethRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readAhadethFile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hadeth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        hadethRecyclerView = requireView().findViewById(R.id.hadethRecyclerView)
        hadethAdapter = HadethAdapter(
            ahadeth,
            onClick = {
                Log.d("HadethFragment", "onBindViewHolder: ${it.title}")
                val intent = Intent(requireContext(), HadethDetailsActivity::class.java)
                intent.putExtra(HadethDetailsActivity.HADETH_KEY, it)
                startActivity(intent)
            })
        hadethRecyclerView.adapter = hadethAdapter
        val layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
        layoutManager.carouselAlignment = CarouselLayoutManager.ALIGNMENT_CENTER
        hadethRecyclerView.layoutManager = layoutManager
        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(hadethRecyclerView)
        hadethRecyclerView.addItemDecoration(
            object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: android.graphics.Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.right = 4
                    outRect.left = 4
                }
            }
        )
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