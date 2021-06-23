package com.example.jobsity.ui.peopledetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.jobsity.JobsityApplication
import com.example.jobsity.R
import com.example.jobsity.databinding.FragmentPeopleDetailsBinding
import com.squareup.picasso.Picasso

class PeopleDetailsFragment : Fragment() {

    private val viewModel: PeopleDetailsViewModel by viewModels {
        PeopleDetailsViewModelFactory((context?.applicationContext as JobsityApplication).peopleDetailsRepository)
    }

    private var _binding: FragmentPeopleDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.get("id").toString()

        //val personPhoto = view.findViewById<ImageView>(R.id.people_detail_photo)
        //val personName = view.findViewById<TextView>(R.id.people_detail_name)
        //val personRecyclerView = view.findViewById<RecyclerView>(R.id.person_recyclerview)

        viewModel.getPeopleDetails(id.toInt())

        viewModel.peopleDetailsData.observe(viewLifecycleOwner, { credits ->
            val idList = mutableListOf<String>()
            if (credits?.image == null) {
                binding.peopleDetailPhoto.setImageResource(R.drawable.ic_no_photo)
            } else {
                Picasso.get().load(credits.image.medium).into(binding.peopleDetailPhoto)
            }
            binding.peopleDetailName.text = credits?.name
            if (credits != null) {
                credits._embedded?.castcredits?.forEach {
                    val path = it._links.show.href
                    idList.add(path.substring(path.lastIndexOf('/') + 1))
                }
                viewModel.getCasting(idList)
            }
        })

        viewModel.castDetailsData.observe(viewLifecycleOwner, { record ->
            Log.d("ret_d", record.toString())
            viewModel.getIdFavorites.observe(viewLifecycleOwner, {
                binding.personRecyclerview.adapter = PeopleDetailsAdapter(
                    record,
                    it
                ) { dataset ->
                    viewModel.updateFavorite(dataset)
                }
            })

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}