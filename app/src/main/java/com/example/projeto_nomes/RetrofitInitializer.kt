package com.example.projeto_nomes

import com.example.projeto_nomes.services.NomePorLocalidadeService
import com.example.projeto_nomes.services.NomePorSexoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    private val retrofitNome:Retrofit

    private val retrofitLocalidade:Retrofit

    init {
        retrofitNome = Retrofit.Builder()
            .baseUrl("https://servicodados.ibge.gov.br/api/v2/censos/nomes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    init {
        retrofitLocalidade = Retrofit.Builder()
            .baseUrl("http://servicodados.ibge.gov.br/api/v1/localidades/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    fun nomePorSexoService(): NomePorSexoService {
        return retrofitNome.create(NomePorSexoService::class.java)
    }

    fun nomePorLocalidadeService(): NomePorLocalidadeService{
        return retrofitLocalidade.create(NomePorLocalidadeService::class.java)
    }


}