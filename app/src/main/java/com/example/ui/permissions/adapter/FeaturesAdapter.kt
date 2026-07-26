package com.example.ui.permissions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.databinding.ItemFeatureSwitchBinding
import com.example.domain.model.FeatureItem

class FeaturesAdapter(
    private val onFeatureToggled: (featureId: String, isEnabled: Boolean) -> Unit
) : ListAdapter<FeatureItem, FeaturesAdapter.FeatureViewHolder>(FeatureDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val binding = ItemFeatureSwitchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FeatureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FeatureViewHolder(
        private val binding: ItemFeatureSwitchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeatureItem) {
            binding.apply {
                tvFeatureName.text = item.displayName
                tvFeatureDescription.text = item.description
                switchFeature.setOnCheckedChangeListener(null)
                switchFeature.isChecked = item.isEnabled
                switchFeature.isEnabled = item.isSupported
                switchFeature.setOnCheckedChangeListener { _, isChecked ->
                    onFeatureToggled(item.id, isChecked)
                }
            }
        }
    }

    class FeatureDiffCallback : DiffUtil.ItemCallback<FeatureItem>() {
        override fun areItemsTheSame(old: FeatureItem, new: FeatureItem) = old.id == new.id
        override fun areContentsTheSame(old: FeatureItem, new: FeatureItem) = old == new
    }
}
