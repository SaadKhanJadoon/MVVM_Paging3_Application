package com.saadkhan.paging3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.saadkhan.paging3.adapter.CharacterAdapter.ImageViewHolder
import com.saadkhan.paging3.databinding.CharacterLayoutBinding
import com.saadkhan.paging3.model.RickMorty
import javax.inject.Inject

class CharacterAdapter @Inject constructor() :
    PagingDataAdapter<RickMorty, ImageViewHolder>(diffCallback) {

    inner class ImageViewHolder(val binding: CharacterLayoutBinding) :
        ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<RickMorty>() {
            override fun areItemsTheSame(oldItem: RickMorty, newItem: RickMorty): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RickMorty, newItem: RickMorty): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            CharacterLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currChar = getItem(position)
        holder.binding.apply {
            holder.itemView.apply {
                tvName.text = "${currChar?.name}"
                tvStatus.text = "${currChar?.status}"
                tvSpecies.text = "${currChar?.species}"
                tvGender.text = "${currChar?.gender}"

                imageView.load(currChar?.image) {
                    crossfade(true)
                    crossfade(1000)
                }
            }
        }
    }
}