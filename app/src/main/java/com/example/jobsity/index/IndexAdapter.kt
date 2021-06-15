package com.example.jobsity.index

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.R
import com.example.jobsity.network.ShowIndex
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

//Recyclerview adapter for shows
//Used for main index and favorites
class IndexAdapter constructor(
    private val dataset: List<ShowIndex>,
    private val favorites: List<Int>,
    //Clicklistener for favorites
    val clickListener: (ShowIndex) -> Unit
) : RecyclerView.Adapter<IndexAdapter.ShowIndexViewHolder>() {

    //Define fields
    class ShowIndexViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var showName: TextView = view.findViewById(R.id.show_name)
        var showGenre: TextView = view.findViewById(R.id.show_genre)
        var showPoster: ImageView = view.findViewById(R.id.show_poster)
        var preferredIcon: ToggleButton = view.findViewById(R.id.show_preferred)
        var detailButton: MaterialCardView = view.findViewById(R.id.show_card)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    )
            : ShowIndexViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_index, parent, false)

        return ShowIndexViewHolder(adapterLayout)
    }

    //Bind data
    override fun onBindViewHolder(holder: ShowIndexViewHolder, position: Int) {
        val item = dataset[position]
        holder.showName.text = item.name
        //Make genre list as string
        holder.showGenre.text = item.genres.joinToString(", ")
        //Load image with Picasso
        Picasso.get().load(item.image?.medium).into(holder.showPoster)

        //Check if its already defined as favorite, and change the icon
        holder.preferredIcon.isChecked = favorites.indexOf(item.id) != -1

        //Clicklistener for favorites
        holder.preferredIcon.setOnClickListener {
            //If clicked, return to fragment the information regarding the show
            //Needed to update the favorites
            clickListener(item)
        }

        //Listener to go to show details
        holder.detailButton.setOnClickListener {
            val bundle = bundleOf("id" to item.id.toString())
            it.findNavController().navigate(R.id.ShowFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}