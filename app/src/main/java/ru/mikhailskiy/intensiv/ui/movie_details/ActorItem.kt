package ru.mikhailskiy.intensiv.ui.movie_details

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.actor_item.*
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie.Actor

class ActorItem (
    private val actor : Actor
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
       viewHolder.actor_name.text = actor.name

        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500${actor.profilePath}")
            .into(viewHolder.actor_profile_image)
    }

    override fun getLayout(): Int = R.layout.actor_item
}