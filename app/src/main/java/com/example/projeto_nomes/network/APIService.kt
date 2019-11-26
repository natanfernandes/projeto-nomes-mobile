package com.example.projeto_nomes.network

import com.example.projeto_nomes.model.NomePorSexo
import com.example.projeto_nomes.model.RankingSexo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("{name}")
    fun buscarNomePorSexo(@Path("name") name:String, @Query("sexo") sex:String):Call<List<NomePorSexo>>

    @GET("ranking/")
    fun buscarRankingPorSexo(@Query("sexo") sex:String):Call<List<RankingSexo>>

    @GET("{name}")
    fun buscarNomePorLocalidade(@Path("name") name:String, @Query("localidade") localidade:String):Call<List<NomePorSexo>>

    companion object {
        val instance: APIService by lazy {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://servicodados.ibge.gov.br/api/v2/censos/nomes/")
                .build()
            retrofit.create(APIService::class.java)
        }

    }
}