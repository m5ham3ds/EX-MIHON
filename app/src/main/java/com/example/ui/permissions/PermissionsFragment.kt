package com.example.ui.permissions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.R
import com.example.databinding.FragmentPermissionsBinding
import com.example.domain.model.AnalysisResult
import com.example.ui.permissions.adapter.FeaturesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
import android.widget.LinearLayout
import com.example.domain.model.ExtensionConfig

@AndroidEntryPoint
class PermissionsFragment : Fragment(R.layout.fragment_permissions) {

    private val viewModel: PermissionsViewModel by viewModels()
    private var _binding: FragmentPermissionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var featuresAdapter: FeaturesAdapter
    private var currentResult: AnalysisResult? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPermissionsBinding.bind(view)

        val targetUrl = arguments?.getString("targetUrl") ?: ""
        viewModel.initUrl(targetUrl)

        setupRecyclerView()
        setupClickListeners()
        observeState()
        observeEvents()
    }

    private fun setupRecyclerView() {
        featuresAdapter = FeaturesAdapter(
            onFeatureToggled = viewModel::onFeatureToggled
        )
        binding.rvFeatures.apply {
            adapter = featuresAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
    
    private fun setupClickListeners() {
        binding.btnBuild.setOnClickListener {
            val result = currentResult ?: return@setOnClickListener
            // Build config manually for now since we don't have all the forms
            val config = ExtensionConfig(
                targetUrl = result.url,
                siteName = result.siteMetadata.siteName,
                siteNameSlug = result.siteMetadata.siteName.replace(Regex("[^a-zA-Z0-9]"), "").lowercase(),
                language = result.siteMetadata.detectedLanguage,
                packageName = "eu.kanade.tachiyomi.extension.${result.siteMetadata.detectedLanguage}.${result.siteMetadata.siteName.replace(Regex("[^a-zA-Z0-9]"), "").lowercase()}",
                versionCode = 1,
                versionName = "1.0",
                chosenTemplate = result.chosenTemplate,
                enabledFeatures = result.compatibleFeatures.filter { it.isEnabled }.map { it.id }.toSet(),
                customSelectors = emptyMap(),
                detectedSelectors = result.detectedSelectors,
                preferences = emptyMap(),
                outputPath = "" // Will be handled by worker
            )
            val bundle = Bundle().apply {
                putSerializable("extensionConfig", config)
            }
            findNavController().navigate(R.id.progressFragment, bundle)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    renderState(state)
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    handleEvent(event)
                }
            }
        }
    }

    private fun renderState(state: AnalysisUiState) {
        when (state) {
            is AnalysisUiState.Idle -> {
                binding.progressBar.visibility = View.GONE
                binding.contentLayout.visibility = View.GONE
            }
            is AnalysisUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.contentLayout.visibility = View.GONE
            }
            is AnalysisUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.contentLayout.visibility = View.VISIBLE
                showSuccessState(state.result)
            }
            is AnalysisUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.contentLayout.visibility = View.GONE
                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_INDEFINITE)
                    .setAction("إعادة المحاولة") {
                        val url = arguments?.getString("targetUrl") ?: ""
                        viewModel.analyzeWebsite(url)
                    }.show()
            }
        }
    }
    
    private fun showSuccessState(result: AnalysisResult) {
        currentResult = result
        binding.tvSiteName.text = "اسم الموقع: ${result.siteMetadata.siteName} (${result.siteMetadata.detectedLanguage})"
        binding.tvTemplateType.text = "نوع القالب: ${result.detectedTemplateType.displayName}"
        binding.confidenceBar.progress = (result.confidenceScore * 100).toInt()
        
        binding.selectorsContainer.removeAllViews()
        result.detectedSelectors.forEach { (key, value) ->
            val textInputLayout = TextInputLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 16)
                }
                hint = key
            }
            val textInputEditText = TextInputEditText(requireContext()).apply {
                setText(value)
            }
            textInputLayout.addView(textInputEditText)
            binding.selectorsContainer.addView(textInputLayout)
        }
        
        featuresAdapter.submitList(result.compatibleFeatures)
    }

    private fun handleEvent(event: PermissionsUiEvent) {
        when (event) {
            is PermissionsUiEvent.NavigateToProgress -> {
                val bundle = Bundle().apply {
                    putSerializable("extensionConfig", event.config)
                }
                findNavController().navigate(R.id.progressFragment, bundle)
            }
            is PermissionsUiEvent.ShowValidationError ->
                Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
