package com.example.projeto_nomes

import com.example.projeto_nomes.services.NomePorSexoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    private val retrofit:Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://servicodados.ibge.gov.br/api/v2/censos/nomes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    fun nomePorSexoService(): NomePorSexoService {
        return retrofit.create(NomePorSexoService::class.java)
    }
}