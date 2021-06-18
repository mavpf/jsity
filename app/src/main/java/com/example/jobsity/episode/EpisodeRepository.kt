package com.example.jobsity.episode

import com.example.jobsity.dataclasses.ShowEpisodesDetails

class EpisodeRepository {

    suspend fun getEpisodeDetails(id: Int): ShowEpisodesDetails {
        return ShowIndexApi.retrofitService.getEpisodeDetails(id)
    }
}