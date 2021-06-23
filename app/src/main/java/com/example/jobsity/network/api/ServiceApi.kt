
import com.example.jobsity.data.classes.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Base URL for API
private const val BASE_URL = "https://api.tvmaze.com"

//Build Moshi JSON
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Build retrofit to get API data
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

//Create interfaces for APIs
interface ShowIndexApiService {

    //Get shows by page
    @GET("shows")
    suspend fun getIndex(
        @Query("page") page : Int
    ): List<ShowIndex>

    //Get shows by name
    @GET("search/shows")
    suspend fun getShowNames(
        @Query("q") q: String
    ): List<ShowNames>

    //Get show details
    @GET("shows/{id}")
    suspend fun getShowDetail(
        @Path("id") id: Int
    ): ShowDetails

    //Get shows episodes and seasons
    @GET("shows/{id}/episodes")
    suspend fun getShowEpisodes(
        @Path("id") id: Int
    ): List<ShowEpisodes>

    //Get episode details
    @GET("episodes/{episodeId}")
    suspend fun getEpisodeDetails(
        @Path("episodeId") episodeId: Int
    ): ShowEpisodesDetails

    //Get people
    @GET("search/people")
    suspend fun searchPerson(
        @Query("q") q: String
    ): List<PersonResult>

    //Get people participation
    @GET("people/{id}")
    suspend fun getPeopleDetails(
        @Path("id") id: Int,
        @Query("embed") cast: String
    ): Credits
}

object ShowIndexApi {
    val retrofitService: ShowIndexApiService by lazy {
        retrofit.create(ShowIndexApiService::class.java)
    }
}


