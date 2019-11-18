package com.example.projeto_nomes.services

import com.example.projeto_nomes.model.NomePorSexo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NomePorLocalidadeService {

    @GET("{name}")
    fun buscarNomePorSexo(@Path("name") name:String, @Query("localidade") sex:String):Call<List<NomePorSexo>>

}