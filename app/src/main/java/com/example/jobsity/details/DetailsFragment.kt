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

        val id = arguments?.getString("id")?.toInt()!!

        viewModel.getShowDetails(id)
        viewModel.getShowEpisodes(id)

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val detailPosterImage: ImageView = view.findViewById(R.id.detail_poster_image)
        val detailShowName: TextView = view.findViewById(R.id.detail_show_name)
        val detailShowGenre: TextView = view.findViewById(R.id.detail_show_genre)
        val detailShowSummary: TextView = view.findViewById(R.id.detail_show_summary)
        val detailShowScheduleTime: TextView = view.findViewById(R.id.detail_show_schedule_time)
        val detailShowScheduleDays: TextView = view.findViewById(R.id.detail_show_schedule_days)

        val episodesRecyclerView: RecyclerView = view.findViewById(R.id.episodes_recycler_view)
        val seasonSpinner: Spinner = view.findViewById(R.id.season_spinner)


        super.onViewCreated(view, savedInstanceState)
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

        viewModel.showEpisodes.observe(viewLifecycleOwner, { episode ->
            val spinnerData = ArrayAdapter<String>(requireContext(), R.layout.season_spinner)
            episode?.forEach {
                if (spinnerData.getPosition("Season " + it.season.toString()) == -1) {
                    spinnerData.add("Season " + it.season.toString())
                }
            }
            seasonSpinner.adapter = spinnerData

            seasonSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.clearEpisodesPerSeason()
                    val season = seasonSpinner.selectedItem.toString().replace("Season ", "")
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