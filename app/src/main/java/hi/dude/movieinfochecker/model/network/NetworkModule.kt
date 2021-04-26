package hi.dude.movieinfochecker.model.network

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun getRetrofit(converter: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://imdb-api.com/en/API/")
            .addConverterFactory(converter)
            .build()

    @Provides
    fun getConvertor() = GsonConverterFactory.create()

    @Provides
    fun getImdbApi(retrofit: Retrofit): ImdbApi = retrofit.create(ImdbApi::class.java)
}