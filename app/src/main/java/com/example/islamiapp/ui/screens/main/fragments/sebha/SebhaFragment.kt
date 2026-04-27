package com.example.islamiapp.ui.screens.main.fragments.sebha


import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import com.example.islamiapp.databinding.FragmentSebhaBinding

class SebhaFragment : Fragment() {

    private lateinit var binding: FragmentSebhaBinding

    private val tasbeehList = listOf("سبحان الله", "الحمد لله", "الله أكبر")
    private var currentTasbeehIndex = 0
    private var count = 0
    private var isTransitioning = false

    private var rotationAnimator: ObjectAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSebhaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        setupBeadsClick()
    }

    private fun setupBeadsClick() {
        binding.sebhaContainer.setOnClickListener {
            onBeadClicked()
        }
    }

    private fun onBeadClicked() {
        if (isTransitioning) return

        if (count < MAX_COUNT) {
            count++
            updateUI()
            animateBeads()
        }

        if (count == MAX_COUNT) {
            isTransitioning = true
            binding.sebhaContainer.postDelayed({
                currentTasbeehIndex = (currentTasbeehIndex + 1) % tasbeehList.size
                count = 0
                isTransitioning = false
                updateUI()
            }, TRANSITION_DELAY)
        }
    }

    private fun updateUI() {
        binding.tasbeehTextView.text = tasbeehList[currentTasbeehIndex]
        binding.counterTextView.text = count.toString()
    }

    private fun animateBeads() {
        rotationAnimator?.cancel()

        val currentRotation = binding.sebhaImageView.rotation

        rotationAnimator = ObjectAnimator.ofFloat(
            binding.sebhaImageView,
            View.ROTATION,
            currentRotation,
            currentRotation + ROTATION_STEP
        ).apply {
            duration = 250
            interpolator = LinearInterpolator()
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rotationAnimator?.cancel()
    }

    private companion object {
        const val MAX_COUNT = 30
        const val TRANSITION_DELAY = 600L
        const val ROTATION_STEP = 12f
    }
}