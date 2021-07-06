package com.example.jobsity.network.api

import com.example.jobsity.data.classes.*

//Helper is needed for Hilt injection
interface ServiceApiHelper {

    suspend fun getIndex(page : Int): List<ShowIndex>

    //Get shows by name
    suspend fun getShowNames(q: String): List<ShowNames>

    //Get show details
    suspend fun getShowDetail(id: Int): ShowDetails

    //Get shows episodes and seasons
    suspend fun getShowEpisodes(id: Int): List<ShowEpisodes>

    //Get episode details
    suspend fun getEpisodeDetails(episodeId: Int): ShowEpisodesDetails

    //Get people
    suspend fun searchPerson(q: String): List<PersonResult>

    //Get people participation
    suspend fun getPeopleDetails(id: Int, cast: String): Credits
}