package com.example.projeto_nomes.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.projeto_nomes.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        buscar_nomes_masculinos.setOnClickListener{
            Log.d("teste","teste")
            val intent = Intent(this,
                BuscarNomesMasculinosActivity::class.java)
            startActivity(intent)
        }
        buscar_nomes_femininos.setOnClickListener{
            Log.d("teste","teste")
            val intent = Intent(this,
                BuscarNomesFemininosActivity::class.java)
            startActivity(intent)
        }
    }
}
