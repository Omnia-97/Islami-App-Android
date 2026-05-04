package com.example.islamiapp.ui.screens.main.fragments.radio

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.islamiapp.databinding.FragmentRadioBinding
import com.example.islamiapp.model.radio.RadioDM
import com.example.islamiapp.network.RetrofitClient
import com.example.islamiapp.service.RadioService
import kotlinx.coroutines.launch

class RadioFragment : Fragment() {

    private lateinit var binding: FragmentRadioBinding
    private lateinit var adapter: RadioAdapter

    private var radioService: RadioService? = null
    private var isBound = false
    private var currentPlayingIndex: Int = -1

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            radioService = (binder as RadioService.RadioBinder).getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            radioService = null
            isBound = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRadioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100
                )
            }
        }

        requireContext().bindService(
            Intent(requireContext(), RadioService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )

        loadRadios()
    }

    private fun loadRadios() {
        binding.progressBar.visibility = View.VISIBLE
        binding.radioRecyclerView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getRadios()
                if (response.isSuccessful && response.body() != null) {
                    val stations = response.body()!!.radios
                    binding.progressBar.visibility = View.GONE
                    binding.radioRecyclerView.visibility = View.VISIBLE
                    initAdapter(stations)
                } else {
                    showError("Failed to load radios")
                }
            } catch (e: Exception) {
                showError("Network error: ${e.message}")
            }
        }
    }

    private fun initAdapter(stations: List<RadioDM>) {
        adapter = RadioAdapter(stations)

        adapter.onPlayClickListener = { station, position ->
            handlePlayPause(station, position)
        }
        adapter.onVolumeClickListener = {
            val newMuteState = radioService?.toggleMute() ?: false
            adapter.setMuted(newMuteState)
        }

        binding.radioRecyclerView.adapter = adapter
    }

    private fun handlePlayPause(station: RadioDM, position: Int) {
        if (currentPlayingIndex == position) {
            stopRadio()
            currentPlayingIndex = -1
        } else {
            stopRadio()
            startRadio(station, position)
        }
    }

    private fun startRadio(station: RadioDM, position: Int) {
        requireContext().startService(
            Intent(requireContext(), RadioService::class.java).apply {
                action = RadioService.ACTION_PLAY
                putExtra(RadioService.EXTRA_URL, station.url)
                putExtra(RadioService.EXTRA_NAME, station.name)
            }
        )
        currentPlayingIndex = position
        adapter.setPlayingIndex(position)
    }

    private fun stopRadio() {
        requireContext().startService(
            Intent(requireContext(), RadioService::class.java).apply {
                action = RadioService.ACTION_STOP
            }
        )
        if (::adapter.isInitialized) adapter.setPlayingIndex(-1)
        currentPlayingIndex = -1
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isBound) {
            requireContext().unbindService(serviceConnection)
            isBound = false
        }
    }
}