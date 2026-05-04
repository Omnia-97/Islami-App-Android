package com.example.islamiapp.ui.screens.main.fragments.radio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.islamiapp.R
import com.example.islamiapp.databinding.ItemRadioBinding
import com.example.islamiapp.model.radio.RadioDM

class RadioAdapter(private val stations: List<RadioDM>) :
    RecyclerView.Adapter<RadioAdapter.RadioViewHolder>() {

    private var playingIndex: Int = -1
    private var isMuted: Boolean = false

    var onPlayClickListener: ((radioDM: RadioDM, position: Int) -> Unit)? = null
    var onVolumeClickListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioViewHolder {
        val binding = ItemRadioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RadioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RadioViewHolder, position: Int) {
        val station = stations[position]
        val isPlaying = position == playingIndex
        holder.bind(station, isPlaying, isMuted)

        holder.binding.playPauseImageView.setOnClickListener {
            val previousPlaying = playingIndex
            playingIndex = if (isPlaying) -1 else position
            isMuted = false
            if (previousPlaying != -1) notifyItemChanged(previousPlaying)
            notifyItemChanged(position)
            onPlayClickListener?.invoke(station, position)
        }

        holder.binding.volumeImageView.setOnClickListener {
            if (isPlaying) {
                onVolumeClickListener?.invoke()
            }
        }
    }

    override fun getItemCount(): Int = stations.size

    fun setPlayingIndex(index: Int) {
        val previous = playingIndex
        playingIndex = index
        isMuted = false
        if (previous != -1) notifyItemChanged(previous)
        if (index != -1) notifyItemChanged(index)
    }

    fun setMuted(muted: Boolean) {
        isMuted = muted
        if (playingIndex != -1) notifyItemChanged(playingIndex)
    }

    class RadioViewHolder(val binding: ItemRadioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(station: RadioDM, isPlaying: Boolean, isMuted: Boolean) {
            binding.radioNameTextView.text = station.name

            binding.radioItemBgImage.setImageResource(
                if (isPlaying) R.drawable.sound_wave else R.drawable.mosque_bg
            )

            binding.playPauseImageView.setImageResource(
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
            )


            binding.volumeImageView.setImageResource(
                if (isPlaying && isMuted) R.drawable.ic_volume_mute else R.drawable.ic_volume
            )

            //binding.volumeImageView.alpha = if (isPlaying) 1f else 0.4f
        }
    }
}