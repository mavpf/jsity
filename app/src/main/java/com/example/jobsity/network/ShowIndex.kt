package com.example.jobsity.network

import com.squareup.moshi.Json

//Dataclasses to handle API information

data class Images(
    val medium: String,
    val original: String
)

data class Schedule(
    val time: String,
    val days: List<String>
)

data class ShowIndex(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "genres") val genres: List<String>,
    @Json(name = "image") val image: Images?
)

data class ShowNames(
    @Json(name = "show") val show: ShowIndex
)

data class ShowDetails(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "genres") val genres: List<String>,
    @Json(name = "image") val image: Images?,
    @Json(name = "summary") val summary: String,
    @Json(name = "schedule") val schedule: Schedule
)

data class ShowEpisodesDetails(
    @Json(name = "name") val name: String,
    @Json(name = "season") val season: Int,
    @Json(name = "number") val number: Int,
    @Json(name = "summary") val summary: String,
    @Json(name = "image") val image: Images?
)

data class ShowEpisodes(
    @Json(name = "id") val idEpisode: Int,
    @Json(name = "name") val name: String,
    @Json(name = "season") val season: Int,
    @Json(name = "number") val number: Int,
    @Json(name = "image") val image: Images?
    )