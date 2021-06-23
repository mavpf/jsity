package com.example.jobsity.ui.episode

import com.example.jobsity.data.classes.ShowEpisodesDetails
import com.example.jobsity.network.api.ServiceApiHelper
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val serviceApiHelper: ServiceApiHelper
) {

    suspend fun getEpisodeDetails(id: Int): ShowEpisodesDetails {
        return serviceApiHelper.getEpisodeDetails(id)
    }
}