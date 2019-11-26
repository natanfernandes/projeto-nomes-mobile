package com.example.projeto_nomes.network

import com.example.projeto_nomes.model.Localidade
import com.example.projeto_nomes.model.NomePorSexo
import com.example.projeto_nomes.model.RankingSexo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APILocalidadeService {
    @GET("estados")
    fun buscarLocalidades():Call<List<Localidade>>

    companion object {
        val instance: APILocalidadeService by lazy {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://servicodados.ibge.gov.br/api/v1/localidades/")
                .build()
            retrofit.create(APILocalidadeService::class.java)
        }

    }
}