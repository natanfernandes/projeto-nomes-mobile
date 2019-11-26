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
import com.example.projeto_nomes.repository.SQLiteRepository2


class MyIntentService2 : IntentService("RankPorSexoService"){

    private var rankRepository: SQLiteRepository2? = null

    override fun onHandleIntent(intent: Intent?) {

        rankRepository = SQLiteRepository2(this)

        var strSexo: String = intent!!.getStringExtra("sexo")
        getRanking(strSexo)
    }

    private fun getRanking(sexo: String) {

        rankRepository?.cleanTables()

        val searchCall = APIService.instance.buscarRankingPorSexo(sexo)
        searchCall.enqueue(object : Callback<List<RankingSexo>> {
            override fun onResponse(call: Call<List<RankingSexo>>, response: Response<List<RankingSexo>>) {
                val nomes = response.body()

                rankRepository?.save(nomes)

                val local = Intent()

                local.action = "com.hello.action2"

                sendBroadcast(local)
            }

            override fun onFailure(call: Call<List<RankingSexo>>, t: Throwable) {
                exibirErro(t)

            }
        })
    }
    private fun exibirErro(t:Throwable){
        Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
    }


}