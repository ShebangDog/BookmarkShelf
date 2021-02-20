package dog.shebang.data.api

import dog.shebang.data.api.entity.MetadataEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface LinkPreviewApi {
    @GET("/")
    suspend fun fetchMetadata(
        @Query("key") key: String,
        @Query("q") url: String
    ): MetadataEntity
}
