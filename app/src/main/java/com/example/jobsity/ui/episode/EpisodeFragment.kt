package com.example.jobsity.ui.episode

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.jobsity.R
import com.example.jobsity.databinding.FragmentEpisodeBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeFragment : Fragment() {

    private val viewModel: EpisodeViewModel by viewModels()
    private var _binding: FragmentEpisodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodeBinding.inflate(inflater, container, false)
        val view = binding.root

        val idEpisode = arguments?.getString("idEpisode")?.toInt()!!

        //Get episode information
        viewModel.getEpisodeDetails(idEpisode)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

        //Load episode information
        viewModel.episodeDetails.observe(viewLifecycleOwner, {
            //Load image with picasso
            Picasso.get()
                .load(it?.image?.original)
                .into(binding.episodeDetailImage)

            //Check if data available, since not all episodes have information

            //Concatenate episode name and number
            if (it?.number != null || it?.name != null) {
                val episodeFull = it.number.toString() + ". " + (it.name)
                binding.episodeDetailName.text = episodeFull
            } else {
                binding.episodeDetailName.text = getString(R.string.name_not_found)
            }

            if (it?.season != null) {
                binding.episodeDetailSeason.text = it.season.toString()
            } else {
                binding.episodeDetailSeason.text = getString(R.string.season_not_found)
            }

            if (it?.summary != null) {
                binding.episodeDetailSummary.text =
                    Html.fromHtml(it.summary, Html.FROM_HTML_MODE_COMPACT)
            } else {
                binding.episodeDetailSummary.text = getString(R.string.summary_not_found)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}