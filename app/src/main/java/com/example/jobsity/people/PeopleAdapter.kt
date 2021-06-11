package com.example.jobsity.people

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.R
import com.example.jobsity.network.PersonResult
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class PeopleAdapter (
    private val dataset: List<PersonResult>
) : RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {

    class PeopleViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.people_photo)
        val name: TextView = view.findViewById(R.id.people_name)
        val card: MaterialCardView = view.findViewById(R.id.people_card)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PeopleViewHolder {
       val adapterLayout = LayoutInflater.from(parent.context)
           .inflate(R.layout.grid_people, parent, false)

        return PeopleViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val item = dataset[position]
        holder.name.text = item.person.name
        if (item.person.image == null) {
            holder.image.setImageResource(R.drawable.ic_no_photo)
        } else {
            Picasso.get().load(item.person.image?.medium).into(holder.image)
        }
        holder.card.setOnClickListener {
            val bundle = bundleOf("id" to item.person.id)
            it.findNavController().navigate(R.id.PeopleDetailsFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}