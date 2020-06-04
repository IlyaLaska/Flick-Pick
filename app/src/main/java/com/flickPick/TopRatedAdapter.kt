package com.flickPick

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flickPick.databinding.ListItemFilmsBinding
import com.google.android.material.shape.CornerFamily


class TopRatedAdapter (val clickListener: FilmListener): ListAdapter<FilmProperty, TopRatedAdapter.ViewHolder>(FilmsListDiffCallback()) {//RecyclerView.Adapter<TopRatedAdapter.ViewHolder>() {
//    var data: List<Map<String, String>> = List<Map<String, String>>()//TODO how to init empty structure??
//    var data = listOf(mapOf("filmName" to "", "filmNameRussian" to "", "filmReleaseYear" to "",
//    "filmRating" to "", "filmGenres" to "")) //List of movies TODO - find how to store them - list of maps with correct data
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

//    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemFilmsBinding): RecyclerView.ViewHolder(binding.root) {
        //TODO do i need ID in ViewHolder?
        fun bind(item: FilmProperty, clickListener: FilmListener) {
            binding.film = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ListItemFilmsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                binding.filmImageBig.shapeAppearanceModel =
                    binding.filmImageBig.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 10f)
                        .setBottomLeftCorner(CornerFamily.ROUNDED, 10f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 10f)
                        .setBottomRightCorner(CornerFamily.ROUNDED, 10f)
                        .build()
//                val view =
//                    LayoutInflater.from(parent.context).inflate(R.layout.list_item_films, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

//TODO - change this based on what i decided to do with filmImage
class FilmsListDiffCallback: DiffUtil.ItemCallback<FilmProperty>() {
    override fun areItemsTheSame(
        oldItem: FilmProperty,
        newItem: FilmProperty
    ): Boolean {
        return (oldItem.id == newItem.id)
    }

    override fun areContentsTheSame(
        oldItem: FilmProperty,
        newItem: FilmProperty
    ): Boolean {
        return oldItem == newItem
    }
}

class FilmListener(val clickListener: (film: FilmProperty) -> Unit) {
    fun onClick(film: FilmProperty) = clickListener(film)
}