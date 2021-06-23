package com.example.jobsity.ui.details

import com.example.jobsity.data.classes.ShowDetails
import com.example.jobsity.data.classes.ShowEpisodes
import com.example.jobsity.network.api.ServiceApiHelper
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val serviceApiHelper: ServiceApiHelper
){
    suspend fun getShowEpisodes(id: Int): List<ShowEpisodes>
    {
        return serviceApiHelper.getShowEpisodes(id)
    }

    suspend fun getShowDetail(id: Int): ShowDetails {
        return serviceApiHelper.getShowDetail(id)
    }
}