package com.example.jobsity.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.R
import com.example.jobsity.databinding.GridPeopleBinding
import com.example.jobsity.network.PersonResult
import com.squareup.picasso.Picasso

class PeopleAdapter (
    private val dataset: List<PersonResult>
) : RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {

    class PeopleViewHolder(val binding: GridPeopleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PeopleViewHolder {
        val binding = GridPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PeopleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        with(holder) {
            val item = dataset[position]
            binding.peopleName.text = item.person.name
            if (item.person.image == null) {
                binding.peoplePhoto.setImageResource(R.drawable.ic_no_photo)
            } else {
                Picasso.get().load(item.person.image.medium).into(binding.peoplePhoto)
            }
            binding.peopleCard.setOnClickListener {
                val bundle = bundleOf("id" to item.person.id)
                it.findNavController().navigate(R.id.PeopleDetailsFragment, bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}