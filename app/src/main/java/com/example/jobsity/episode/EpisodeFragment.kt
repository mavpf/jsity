package com.example.jobsity.episode

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.jobsity.R
import com.squareup.picasso.Picasso

class EpisodeFragment : Fragment() {

    private val viewModel: EpisodeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_episode, container, false)

        val idEpisode = arguments?.getString("idEpisode")?.toInt()!!

        //Get episode information
        viewModel.getEpisodeDetails(idEpisode)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        val episodeImage: ImageView = view.findViewById(R.id.episode_detail_image)
        val episodeName: TextView = view.findViewById(R.id.episode_detail_name)
        val episodeSeason: TextView = view.findViewById(R.id.episode_detail_season)
        val episodeSummary: TextView = view.findViewById(R.id.episode_detail_summary)

        //Load episode information
        viewModel.episodeDetails.observe(viewLifecycleOwner, {
            //Load image with picasso
            Picasso.get()
                .load(it?.image?.original)
                .into(episodeImage)

            //Check if data available, since not all episodes have information

            //Concatenate episode name and number
            if (it?.number != null || it?.name!= null ){
                val episodeFull = it?.number.toString() + ". " + (it?.name)
                episodeName.text = episodeFull
            } else {
                episodeName.text = getString(R.string.name_not_found)
            }

            if (it?.season != null) {
                episodeSeason.text = it?.season.toString()
            } else {
                episodeSeason.text = getString(R.string.season_not_found)
            }

            if (it?.summary != null) {
                episodeSummary.text = Html.fromHtml(it?.summary, Html.FROM_HTML_MODE_COMPACT)
            } else {
                episodeSummary.text = getString(R.string.summary_not_found)
            }
        })
    }
}