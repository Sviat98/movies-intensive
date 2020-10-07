package ru.mikhailskiy.intensiv.ui.tvshows

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import kotlinx.android.synthetic.main.tv_show_item.*
import ru.mikhailskiy.intensiv.data.TvShow
import ru.mikhailskiy.intensiv.R

class TvShowItem(
   private val content : TvShow
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tv_show_name.text = content.title
        viewHolder.tv_show_rating.rating = content.rating

        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500${content.posterPath}")
            .into(viewHolder.tv_show_image)
    }

    override fun getLayout(): Int {
        return R.layout.tv_show_item
    }
}