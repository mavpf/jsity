package com.example.jobsity.details

import com.example.jobsity.dataclasses.ShowDetails
import com.example.jobsity.dataclasses.ShowEpisodes

class DetailsRepository {
    suspend fun getShowEpisodes(id: Int): List<ShowEpisodes>
    {
        return ShowIndexApi.retrofitService.getShowEpisodes(id)
    }

    suspend fun getShowDetail(id: Int): ShowDetails {
        return ShowIndexApi.retrofitService.getShowDetail(id)
    }
}