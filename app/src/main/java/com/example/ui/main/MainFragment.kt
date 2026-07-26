package com.example.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.snackbar.Snackbar
import android.webkit.URLUtil

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        binding.btnAnalyze.setOnClickListener {
            val url = binding.etUrl.text.toString().trim()
            if (url.isEmpty() || !URLUtil.isValidUrl(url)) {
                binding.urlInputLayout.error = "الرجاء إدخال رابط صحيح"
            } else {
                binding.urlInputLayout.error = null
                val bundle = Bundle().apply {
                    putString("targetUrl", url)
                }
                findNavController().navigate(R.id.permissionsFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
