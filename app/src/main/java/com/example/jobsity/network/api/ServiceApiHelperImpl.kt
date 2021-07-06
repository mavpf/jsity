package com.example.jobsity.network.api

import com.example.jobsity.data.classes.*
import javax.inject.Inject

//Helper injection implementation for Hilt
class ServiceApiHelperImpl @Inject constructor(
    private val serviceApi: ServiceApi
): ServiceApiHelper {

    override suspend fun getEpisodeDetails(episodeId: Int): ShowEpisodesDetails =
        serviceApi.getEpisodeDetails(episodeId)

    override suspend fun getIndex(page: Int): List<ShowIndex> =
        serviceApi.getIndex(page)

    override suspend fun getPeopleDetails(id: Int, cast: String): Credits =
        serviceApi.getPeopleDetails(id, cast)

    override suspend fun getShowDetail(id: Int): ShowDetails =
        serviceApi.getShowDetail(id)

    override suspend fun getShowEpisodes(id: Int): List<ShowEpisodes> =
        serviceApi.getShowEpisodes(id)

    override suspend fun getShowNames(q: String): List<ShowNames> =
        serviceApi.getShowNames(q)

    override suspend fun searchPerson(q: String): List<PersonResult> =
        serviceApi.searchPerson(q)
}