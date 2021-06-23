package com.example.jobsity.ui.episode

import com.example.jobsity.data.classes.ShowEpisodesDetails

class EpisodeRepository {

    suspend fun getEpisodeDetails(id: Int): ShowEpisodesDetails {
        return ShowIndexApi.retrofitService.getEpisodeDetails(id)
    }
}