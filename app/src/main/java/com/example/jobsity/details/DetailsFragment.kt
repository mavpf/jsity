package com.example.jobsity.details

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.R
import com.squareup.picasso.Picasso


class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_details, container, false)

        //Receive arguments from previous fragment, in order to identify the show.
        val id = arguments?.getString("id")?.toInt()!!

        //Get show info
        viewModel.getShowDetails(id)
        //Get episodes and seasons from the show
        viewModel.getShowEpisodes(id)

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //View Show
        val detailPosterImage: ImageView = view.findViewById(R.id.detail_poster_image)
        val detailShowName: TextView = view.findViewById(R.id.detail_show_name)
        val detailShowGenre: TextView = view.findViewById(R.id.detail_show_genre)
        val detailShowSummary: TextView = view.findViewById(R.id.detail_show_summary)
        val detailShowScheduleTime: TextView = view.findViewById(R.id.detail_show_schedule_time)
        val detailShowScheduleDays: TextView = view.findViewById(R.id.detail_show_schedule_days)

        //View Episodes
        val episodesRecyclerView: RecyclerView = view.findViewById(R.id.episodes_recycler_view)
        val seasonSpinner: Spinner = view.findViewById(R.id.season_spinner)

        super.onViewCreated(view, savedInstanceState)

        //Get API data and load the show information
        viewModel.showDetails.observe(viewLifecycleOwner, { show ->
            Picasso.get()
                .load(show?.image?.original)
                .resize(340, 500)
                .into(detailPosterImage)
            detailShowName.text = show?.name
            detailShowGenre.text = show?.genres?.joinToString(", ")
            detailShowSummary.text = Html.fromHtml(show?.summary, Html.FROM_HTML_MODE_COMPACT)
            detailShowScheduleDays.text = show?.schedule?.days?.joinToString("/n")
            detailShowScheduleTime.text = show?.schedule?.time
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
            seasonSpinner.adapter = spinnerData

            //Based on spinner selection, show the episodes from the season
            seasonSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.clearEpisodesPerSeason()
                    val season = seasonSpinner.selectedItem.toString().replace(getString(R.string.season), "")
                    //Based on spinner selection, get specific data in viewmodel
                    episode?.forEach {
                        if (season.toInt() == it.season) {
                            viewModel.addEpisodesPerSeason(it)
                        }
                    }
                    episodesRecyclerView.adapter = DetailsAdapter(viewModel.getEpisodesPerSeason())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        })

    }

}