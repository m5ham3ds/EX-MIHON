package com.example.ui.progress

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.R
import com.example.databinding.FragmentProgressBinding
import com.example.domain.model.ExtensionConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar

@AndroidEntryPoint
class ProgressFragment : Fragment(R.layout.fragment_progress) {

    private val viewModel: ProgressViewModel by viewModels()
    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProgressBinding.bind(view)

        val config = arguments?.getSerializable("extensionConfig") as? ExtensionConfig
        if (config != null) {
            viewModel.startBuild(config)
        } else {
            Snackbar.make(view, "بيانات الإضافة غير صالحة", Snackbar.LENGTH_LONG).show()
        }

        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    when (state) {
                        is BuildUiState.Idle -> {
                            binding.tvCurrentStep.text = "في الانتظار..."
                            binding.progressBar.progress = 0
                            binding.btnShare.visibility = View.GONE
                        }
                        is BuildUiState.InProgress -> {
                            binding.tvCurrentStep.text = state.currentStep.displayName
                            binding.progressBar.progress = state.progress
                            binding.tvLogs.text = state.logs.joinToString("\n")
                            binding.btnShare.visibility = View.GONE
                        }
                        is BuildUiState.Success -> {
                            binding.tvCurrentStep.text = "🎉 تم بناء الإضافة بنجاح!"
                            binding.progressBar.progress = 100
                            binding.btnShare.visibility = View.VISIBLE
                            binding.btnShare.setOnClickListener {
                                // Real share functionality goes here
                            }
                        }
                        is BuildUiState.Failed -> {
                            binding.tvCurrentStep.text = "❌ فشل البناء"
                            binding.tvLogs.text = state.logContent
                            binding.btnShare.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
