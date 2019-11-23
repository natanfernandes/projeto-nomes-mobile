package com.android.activity

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.widget.Toast

import com.example.projeto_nomes.model.NomePorSexo
import com.example.projeto_nomes.network.APIService
import com.example.projeto_nomes.repository.SQLiteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyIntentService : IntentService("ServiceTwitter"){

    private var nomesRepository: SQLiteRepository? = null

    override fun onHandleIntent(intent: Intent?) {

        nomesRepository = SQLiteRepository(this)

        var strUser: String = intent!!.getStringExtra("nome")
        var strSexo: String = intent.getStringExtra("sexo")
        Toast.makeText(this, strUser, Toast.LENGTH_SHORT).show()
        Toast.makeText(this, strSexo, Toast.LENGTH_SHORT).show()
        getData(strUser, strSexo)

    }

    private fun getData(nome: String, sexo: String) {

        nomesRepository?.cleanTables()

        val searchCall = APIService.instance.buscarNomePorSexo(nome,sexo)
        searchCall.enqueue(object : Callback<List<NomePorSexo>> {

            override fun onResponse(call: Call<List<NomePorSexo>>, response: Response<List<NomePorSexo>>) {
                var nomes = response.body();

                for (nome in nomes!! ){
                    Log.d("teste2",nome.toString())
                    nomesRepository?.save(nome)
                }
            }

            override fun onFailure(call: Call<List<NomePorSexo>>, t: Throwable) {
                exibirErro(t)
            }
        })
    }
    private fun exibirErro(t:Throwable){
        Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
    }



}