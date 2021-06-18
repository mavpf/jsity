package com.example.jobsity.dataclasses

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
    @Json(name = "summary") val summary: String?,
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


data class PersonResult(
    @Json(name = "person") val person: PersonResultSearch
)

data class PersonResultSearch(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "image") val image: Images?
)

data class Credits(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "image") val image: Images?,
    @Json(name = "_embedded") val _embedded: CastCredits?
)
data class CastCredits(
    @Json(name = "castcredits") val castcredits: List<CreditsDetails>
)

data class CreditsDetails(
    @Json(name = "_links") val _links: CreditsShow
)

data class CreditsShow(
    @Json(name = "show") val show: CreditsHref
)

data class CreditsHref(
    @Json(name = "href") val href: String
)