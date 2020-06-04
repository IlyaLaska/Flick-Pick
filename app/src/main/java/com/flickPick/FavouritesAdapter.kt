package com.flickPick

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flickPick.databinding.ListItemFilmsSeriesBinding
import com.google.android.material.shape.CornerFamily

class FavouritesAdapter (val clickListener: FilmOrSeriesListener): ListAdapter<FilmOrSeries, FavouritesAdapter.ViewHolder>(FilmsOrSeriesListDiffCallback()) {//RecyclerView.Adapter<TopRatedAdapter.ViewHolder>() {
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

    class ViewHolder private constructor(val binding: ListItemFilmsSeriesBinding): RecyclerView.ViewHolder(binding.root) {
        //TODO do i need ID in ViewHolder?
        fun bind(item: FilmOrSeries, clickListener: FilmOrSeriesListener) {
            binding.film = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ListItemFilmsSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
class FilmsOrSeriesListDiffCallback: DiffUtil.ItemCallback<FilmOrSeries>() {
    override fun areItemsTheSame(
        oldItem: FilmOrSeries,
        newItem: FilmOrSeries
    ): Boolean {
        return (oldItem.id == newItem.id)
    }

    override fun areContentsTheSame(
        oldItem: FilmOrSeries,
        newItem: FilmOrSeries
    ): Boolean {
        return oldItem == newItem
    }
}

class FilmOrSeriesListener(val clickListener: (film: FilmOrSeries) -> Unit) {
    fun onClick(film: FilmOrSeries) = clickListener(film)
}