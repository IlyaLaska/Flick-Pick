package com.flickPick

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("filmName")
fun TextView.setFilmName(item: FilmProperty?) {
    item?.let {
        text = if(item.filmName.isNotBlank()) item.filmName
        else item.seriesName
    }
}

@BindingAdapter("filmOriginalName")
fun TextView.setFilmOriginalName(item: FilmProperty?) {
    item?.let {
        text = if(item.filmName.isNotBlank()) "${item.filmOriginalLanguage.toUpperCase()}:  ${item.filmOriginalTitle}"
        else "${item.filmOriginalLanguage.toUpperCase()}:  ${item.seriesOriginalTitle}"
    }
}

@BindingAdapter("filmReleaseDate")
fun TextView.setFilmReleaseDate(item: FilmProperty?) {
    item?.let {
        text = if(item.filmName.isNotBlank()) item.filmReleaseDate
        else item.seriesReleaseDate
    }
}

@BindingAdapter("filmRating")
fun TextView.setFilmRating(item: FilmProperty?) {
    item?.let {
        text = item.filmRating
    }
}

@BindingAdapter("filmGenres")
fun TextView.setFilmGenres(item: FilmProperty?) {
    item?.let {
        text = item.filmGenres
    }
}

@BindingAdapter("filmDescription")
fun TextView.setFilmDescription(item: FilmProperty?) {
    item?.let {
        text = item.filmDescription
    }
}

////TODO change Binding Adapter for Image

//TODO change Binding Adapter for Image
@BindingAdapter("filmImage")
fun bindImage(imgView: ImageView,item: FilmProperty?) {
    val imgUrl = "https://image.tmdb.org/t/p/w342${item?.filmImagePath}"
    Log.i("URI:", imgUrl)
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context).load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.film_cover_placeholder_100x147)
                .error(R.drawable.film_cover_placeholder_100x147)
                )
            .into(imgView)
    }
}

@BindingAdapter("filmImageBig")
fun bindImageBig(imgView: ImageView,item: FilmProperty?) {
    val imgUrl = "https://image.tmdb.org/t/p/w780${item?.filmImagePath}"
    Log.i("URI:", imgUrl)
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context).load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.film_cover_placeholder_360x530)
                .error(R.drawable.film_cover_placeholder_360x530)
            )
            .into(imgView)
    }
}

//@BindingAdapter("filmImage")
//fun ImageView.setFilmImage(item: FilmProperty?) {
//    item?.let {
////        text = item["filmName"]
//        setImageResource(R.drawable.film_cover_placeholder_100x147)
//    }
//}

//@BindingAdapter("filmName")
//fun TextView.setFilmName(item: Map<String, String>?) {
//    item?.let {
//        text = item["filmName"]
//    }
//}
//
//@BindingAdapter("filmNameRussian")
//fun TextView.setFilmNameRussian(item: Map<String, String>?) {
//    item?.let {
//        text = item["filmNameRussian"]
//    }
//}
//
//@BindingAdapter("filmReleaseDate")
//fun TextView.setFilmReleaseDate(item: Map<String, String>?) {
//    item?.let {
//        text = item["filmReleaseDate"]
//    }
//}
//
//@BindingAdapter("filmRating")
//fun TextView.setFilmRating(item: Map<String, String>?) {
//    item?.let {
//        text = item["filmRating"]
//    }
//}
//
//@BindingAdapter("filmGenres")
//fun TextView.setFilmGenres(item: Map<String, String>?) {
//    item?.let {
//        text = item["filmGenres"]
//    }
//}


/*
            //TODO do i need ID in ViewHolder?
            binding.filmNameRussian.text = item["filmNameRussian"]
            binding.filmReleaseDate.text = item["filmReleaseYear"]
            binding.filmRating.text = item["filmRating"]
            binding.filmGenres.text = item["filmGenres"]
            //TODO add a correct drawable - maybe store a separate list of links to pictures
            binding.filmImage.setImageResource(R.drawable.film_cover_placeholder_100x147)//.setImageResource(R.drawable.star_btn)
 */

@BindingAdapter("filmName")
fun TextView.setFilmName(item: FilmOrSeries?) {
    item?.let {
        text = if(item.filmName.isNotBlank()) item.filmName
        else item.seriesName
    }
}

@BindingAdapter("filmOriginalName")
fun TextView.setFilmOriginalName(item: FilmOrSeries?) {
    item?.let {
        text = if(item.filmName.isNotBlank()) "${item.filmOriginalLanguage.toUpperCase()}:  ${item.filmOriginalTitle}"
        else "${item.filmOriginalLanguage.toUpperCase()}:  ${item.seriesOriginalTitle}"
    }
}

@BindingAdapter("filmReleaseDate")
fun TextView.setFilmReleaseDate(item: FilmOrSeries?) {
    item?.let {
        text = if(item.filmName.isNotBlank()) item.filmReleaseDate
        else item.seriesReleaseDate
    }
}

@BindingAdapter("filmRating")
fun TextView.setFilmRating(item: FilmOrSeries?) {
    item?.let {
        text = item.filmRating
    }
}

@BindingAdapter("filmGenres")
fun TextView.setFilmGenres(item: FilmOrSeries?) {
    item?.let {
        text = item.filmGenres
    }
}

@BindingAdapter("filmDescription")
fun TextView.setFilmDescription(item: FilmOrSeries?) {
    item?.let {
        text = item.filmDescription
    }
}

////TODO change Binding Adapter for Image

//TODO change Binding Adapter for Image
@BindingAdapter("filmImage")
fun bindImage(imgView: ImageView,item: FilmOrSeries?) {
    val imgUrl = "https://image.tmdb.org/t/p/w342${item?.filmImagePath}"
    Log.i("URI:", imgUrl)
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context).load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.film_cover_placeholder_100x147)
                .error(R.drawable.film_cover_placeholder_100x147)
            )
            .into(imgView)
    }
}

@BindingAdapter("filmImageBig")
fun bindImageBig(imgView: ImageView,item: FilmOrSeries?) {
    val imgUrl = "https://image.tmdb.org/t/p/w780${item?.filmImagePath}"
    Log.i("URI:", imgUrl)
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context).load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.film_cover_placeholder_360x530)
                .error(R.drawable.film_cover_placeholder_360x530)
            )
            .into(imgView)
    }
}