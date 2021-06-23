package com.example.jobsity.ui.details

import com.example.jobsity.data.classes.ShowDetails
import com.example.jobsity.data.classes.ShowEpisodes

class DetailsRepository {
    suspend fun getShowEpisodes(id: Int): List<ShowEpisodes>
    {
        return ShowIndexApi.retrofitService.getShowEpisodes(id)
    }

    suspend fun getShowDetail(id: Int): ShowDetails {
        return ShowIndexApi.retrofitService.getShowDetail(id)
    }
}