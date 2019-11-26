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
import android.content.BroadcastReceiver
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R.attr.name
import android.content.Context
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R.attr.name
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.projeto_nomes.model.RankingSexo


class MyIntentService4 : IntentService("NomePorLocalidadeService"){

    private var nomesRepository: SQLiteRepository? = null

    override fun onHandleIntent(intent: Intent?) {

        nomesRepository = SQLiteRepository(this)

        var strUser: String = intent!!.getStringExtra("nome")
        var strLocal: String = intent!!.getStringExtra("localidade")



        getDataLocalidade(strUser,strLocal)
    }

    private fun getDataLocalidade(nome: String, localidade: String) {

        nomesRepository?.cleanTables()

        val searchCall = APIService.instance.buscarNomePorLocalidade(nome,localidade)
        searchCall.enqueue(object : Callback<List<NomePorSexo>> {
            override fun onResponse(call: Call<List<NomePorSexo>>, response: Response<List<NomePorSexo>>) {
                val nomes = response.body()
                for (nome in nomes!! ){
                    nomesRepository?.save(nome)
                }
                val local = Intent()

                local.action = "getNome"

                sendBroadcast(local)
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