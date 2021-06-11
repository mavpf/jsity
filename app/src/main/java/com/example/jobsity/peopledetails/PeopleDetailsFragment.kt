package com.example.jobsity.peopledetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.JobsityApplication
import com.example.jobsity.R
import com.example.jobsity.db.FavoritesViewModel
import com.example.jobsity.db.FavoritesViewModelFactory
import com.squareup.picasso.Picasso

class PeopleDetailsFragment: Fragment() {

    val viewModel: PeopleDetailsViewModel by viewModels()

    private val favoritesViewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory((context?.applicationContext as JobsityApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_people_details, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.get("id").toString()

        val personPhoto = view.findViewById<ImageView>(R.id.people_detail_photo)
        val personName = view.findViewById<TextView>(R.id.people_detail_name)
        val personRecyclerView = view.findViewById<RecyclerView>(R.id.person_recyclerview)

        viewModel.getPeopleDetails(id.toInt())

        viewModel.peopleDetailsData.observe(viewLifecycleOwner, {
            val idList = mutableListOf<String>()
            if (it?.image == null){
                personPhoto.setImageResource(R.drawable.ic_no_photo)
            } else {
                Picasso.get().load(it.image?.medium).into(personPhoto)
            }
            personName.text = it?.name
            if (it != null) {
                it._embedded?.castcredits?.forEach {
                    val path = it._links.show.href
                    idList.add(path.substring(path.lastIndexOf('/') + 1))
                }
                viewModel.getCasting(idList)
            }
        })

        viewModel.castDetailsData.observe(viewLifecycleOwner, { record ->
            Log.d("ret_d", record.toString())
            favoritesViewModel.getIdFavorites.observe(viewLifecycleOwner, {
                personRecyclerView.adapter = PeopleDetailsAdapter(
                    record,
                    it) { dataset->
                        favoritesViewModel.updateFavorite(dataset)
                }
            })

        })

    }

}