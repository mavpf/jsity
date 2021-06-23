package com.example.jobsity.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.R
import com.example.jobsity.databinding.GridEpisodesBinding
import com.example.jobsity.data.classes.ShowEpisodes
import com.squareup.picasso.Picasso

//Recycler view adapter for episodes
class DetailsAdapter(
    private val dataset: List<ShowEpisodes>
) : RecyclerView.Adapter<DetailsAdapter.EpisodesViewHolder>() {

    //Define fields
    class EpisodesViewHolder(val binding: GridEpisodesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    )
            : EpisodesViewHolder {

        val binding = GridEpisodesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return EpisodesViewHolder(binding)
    }

    //Bind data
    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        with(holder) {
            val item = dataset[position]
            //Concatenate two information, episode number and name
            val episodeNameNumber = item.number.toString() + ". " + item.name
            //Load image with Picasso
            Picasso.get().load(item.image?.medium).into(binding.episodePoster)
            binding.episodeName.text = episodeNameNumber

            //Make the card clickable, to more information regarding episode
            binding.episodeCard.setOnClickListener {
                val bundle = bundleOf("idEpisode" to item.idEpisode.toString())
                it.findNavController().navigate(R.id.EpisodeFragment, bundle)
            }
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}