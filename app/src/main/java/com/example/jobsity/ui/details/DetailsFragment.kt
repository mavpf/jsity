package com.example.jobsity.ui.details

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.jobsity.R
import com.example.jobsity.databinding.FragmentDetailsBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //val root = inflater.inflate(R.layout.fragment_details, container, false)
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        //Receive arguments from previous fragment, in order to identify the show.
        val id = arguments?.getString("id")?.toInt()!!

        //Get show info
        viewModel.getShowDetails(id)
        //Get episodes and seasons from the show
        viewModel.getShowEpisodes(id)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

        //Get API data and load the show information
        viewModel.showDetails.observe(viewLifecycleOwner, { show ->
            Picasso.get()
                .load(show?.image?.original)
                .resize(340, 500)
                .into(binding.detailPosterImage)
            binding.detailShowName.text = show?.name
            binding.detailShowGenre.text = show?.genres?.joinToString(", ")
            binding.detailShowSummary.text =
                Html.fromHtml(show?.summary, Html.FROM_HTML_MODE_COMPACT)
            binding.detailShowScheduleDays.text = show?.schedule?.days?.joinToString("/n")
            binding.detailShowScheduleTime.text = show?.schedule?.time
        })

        //Get season and episodes data
        viewModel.showEpisodes.observe(viewLifecycleOwner, { episode ->
            //Load a spinner with the seasons
            val spinnerData = ArrayAdapter<String>(requireContext(), R.layout.season_spinner)
            episode?.forEach {
                if (spinnerData.getPosition(getString(R.string.season) + it.season.toString()) == -1) {
                    spinnerData.add(getString(R.string.season) + it.season.toString())
                }
            }
            binding.seasonSpinner.adapter = spinnerData

            //Based on spinner selection, show the episodes from the season
            binding.seasonSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.clearEpisodesPerSeason()
                        val season = binding.seasonSpinner.selectedItem.toString()
                            .replace(getString(R.string.season), "")
                        //Based on spinner selection, get specific data in viewModel
                        episode?.forEach {
                            if (season.toInt() == it.season) {
                                viewModel.addEpisodesPerSeason(it)
                            }
                        }
                        binding.episodesRecyclerView.adapter =
                            DetailsAdapter(viewModel.getEpisodesPerSeason())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}