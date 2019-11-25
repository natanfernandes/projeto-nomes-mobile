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






class MyIntentService : IntentService("NomePorSexoService"){

    private var nomesRepository: SQLiteRepository? = null
    private val ACTION_STRING_SERVICE = "ToService"
    private val ACTION_STRING_ACTIVITY = "ToActivity"

    override fun onHandleIntent(intent: Intent?) {

        nomesRepository = SQLiteRepository(this)

        var strUser: String = intent!!.getStringExtra("nome")
        var strSexo: String = intent!!.getStringExtra("sexo")
        getData(strUser, strSexo)
    }

    private fun getData(nome: String, sexo: String) {

        nomesRepository?.cleanTables()

        val searchCall = APIService.instance.buscarNomePorSexo(nome,sexo)
        searchCall.enqueue(object : Callback<List<NomePorSexo>> {
            override fun onResponse(call: Call<List<NomePorSexo>>, response: Response<List<NomePorSexo>>) {
                val nomes = response.body()
                Log.d("entrou",nomes?.get(0).toString())
                for (nome in nomes!! ){
                    Log.d("entrou",nome.localidade.toString())
                    nomesRepository?.save(nome)
                }
                val local = Intent()

                local.action = "com.hello.action"

                sendBroadcast(local)
            }

            override fun onFailure(call: Call<List<NomePorSexo>>, t: Throwable) {
                exibirErro(t)
                Log.d("entrou","entrouerr")

            }
        })
    }
    private fun exibirErro(t:Throwable){
        Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
    }


}