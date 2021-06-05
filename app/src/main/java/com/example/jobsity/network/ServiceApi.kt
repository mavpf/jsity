
import com.example.jobsity.network.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

annotation class NullToEmptyString

private const val BASE_URL = "https://api.tvmaze.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ShowIndexApiService {
    @GET("shows")
    suspend fun getIndex(
        @Query("page") page : Int
    ): List<ShowIndex>

    @GET("search/shows")
    suspend fun getShowNames(
        @Query("q") q: String
    ): List<ShowNames>

    @GET("shows/{id}")
    suspend fun getShowDetail(
        @Path("id") id: Int
    ): ShowDetails

    @GET("shows/{id}/episodes")
    suspend fun getShowEpisodes(
        @Path("id") id: Int
    ): List<ShowEpisodes>

    @GET("episodes/{episodeId}")
    suspend fun getEpisodeDetails(
        @Path("episodeId") episodeId: Int
    ): ShowEpisodesDetails
}

object ShowIndexApi {
    val retrofitService: ShowIndexApiService by lazy {
        retrofit.create(ShowIndexApiService::class.java)
    }
}


