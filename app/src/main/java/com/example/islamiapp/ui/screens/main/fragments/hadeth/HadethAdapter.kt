package com.example.islamiapp.ui.screens.main.fragments.hadeth

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.islamiapp.R
import com.example.islamiapp.model.HadethDm

class HadethAdapter(val hadeth: MutableList<HadethDm>,val  onClick : (HadethDm) -> Unit) :
    RecyclerView.Adapter<HadethAdapter.HadethViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HadethViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ahadeth, parent, false)
        return HadethViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: HadethViewHolder,
        position: Int
    ) {
        holder.bind(position)

    }

    override fun getItemCount(): Int = hadeth.size

    inner class HadethViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hadethText: TextView = itemView.findViewById<TextView>(R.id.hadethTitle)
        val hadethContent: TextView = itemView.findViewById<TextView>(R.id.hadethContent)
        fun bind(position: Int) {
            val hadeth = hadeth[position]
            hadethText.text = hadeth.title
            hadethContent.text = hadeth.content
            itemView.setOnClickListener {
                onClick(hadeth)
                Log.d("HadethAdapter", "onBindViewHolder: $position")
            }
        }
    }
}