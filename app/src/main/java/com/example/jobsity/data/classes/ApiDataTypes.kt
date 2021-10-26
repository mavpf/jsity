package com.example.jobsity.data.classes

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
    val id: Int,
    val name: String,
    val genres: List<String>,
    val image: Images?
)

data class ShowNames(
    val show: ShowIndex
)

data class ShowDetails(
    val id: Int,
    val name: String,
    val genres: List<String>,
    val image: Images?,
    val summary: String?,
    val schedule: Schedule
)

data class ShowEpisodesDetails(
    val name: String,
    val season: Int,
    val number: Int,
    val summary: String,
    val image: Images?
)

data class ShowEpisodes(
    @Json(name = "id") val idEpisode: Int,
    val name: String,
    val season: Int,
    val number: Int,
    val image: Images?
    )


data class PersonResult(
    val person: PersonResultSearch
)

data class PersonResultSearch(
    val id: Int,
    val name: String,
    val image: Images?
)

data class Credits(
    val id: Int,
    val name: String,
    val image: Images?,
    val _embedded: CastCredits?
)
data class CastCredits(
    val castcredits: List<CreditsDetails>
)

data class CreditsDetails(
    val _links: CreditsShow
)

data class CreditsShow(
    val show: CreditsHref
)

data class CreditsHref(
    val href: String
)