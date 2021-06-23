package com.example.jobsity.ui.peopledetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.R
import com.example.jobsity.databinding.GridIndexBinding
import com.example.jobsity.data.classes.ShowIndex
import com.squareup.picasso.Picasso

class PeopleDetailsAdapter(
    private val dataset: List<ShowIndex>,
    private val favorites: List<Int>,
    //Clicklistener for favorites
    val clickListener: (ShowIndex) -> Unit
) : RecyclerView.Adapter<PeopleDetailsAdapter.PeopleDetailsAdapterViewHolder>() {

    class PeopleDetailsAdapterViewHolder(val binding: GridIndexBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PeopleDetailsAdapterViewHolder {

        val binding = GridIndexBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PeopleDetailsAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PeopleDetailsAdapterViewHolder,
        position: Int
    ) {
        with(holder) {
            val item = dataset[position]
            binding.showName.text = item.name
            //Make genre list as string
            binding.showGenre.text = item.genres.joinToString(", ")
            //Load image with Picasso
            Picasso.get().load(item.image?.medium).into(binding.showPoster)

            //Check if its already defined as favorite, and change the icon
            binding.showPreferred.isChecked = favorites.indexOf(item.id) != -1

            //Clicklistener for favorites
            binding.showPreferred.setOnClickListener {
                //If clicked, return to fragment the information regarding the show
                //Needed to update the favorites
                clickListener(item)
            }

            //Listener to go to show details
            binding.showCard.setOnClickListener {
                val bundle = bundleOf("id" to item.id.toString())
                it.findNavController().navigate(R.id.ShowFragment, bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.count()
    }
}