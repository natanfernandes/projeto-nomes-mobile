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
import com.example.projeto_nomes.model.Localidade
import com.example.projeto_nomes.model.RankingSexo
import com.example.projeto_nomes.network.APILocalidadeService
import com.example.projeto_nomes.repository.SQLiteRepository2
import com.example.projeto_nomes.repository.SQLiteRepository3


class MyIntentService3 : IntentService("LocalidadesService"){

    private var localidadeRepository: SQLiteRepository3? = null

    override fun onHandleIntent(intent: Intent?) {

        localidadeRepository = SQLiteRepository3(this)

        getLocalidades()
    }

    private fun getLocalidades() {

        localidadeRepository?.cleanTables()

        val searchCall = APILocalidadeService.instance.buscarLocalidades()
        searchCall.enqueue(object : Callback<List<Localidade>> {
            override fun onResponse(call: Call<List<Localidade>>, response: Response<List<Localidade>>) {
                val nomes = response.body()

                localidadeRepository?.save(nomes)

                val local = Intent()

                local.action = "getLocalidades"

                sendBroadcast(local)
            }

            override fun onFailure(call: Call<List<Localidade>>, t: Throwable) {
                exibirErro(t)

            }
        })
    }
    private fun exibirErro(t:Throwable){
        Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
    }


}