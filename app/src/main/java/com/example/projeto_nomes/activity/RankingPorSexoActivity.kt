package com.example.projeto_nomes.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.android.activity.MyIntentService2
import com.example.projeto_nomes.R
import com.example.projeto_nomes.model.RankingSexo
import com.example.projeto_nomes.repository.SQLiteRepository2
import com.example.recyclerviewt12.Message
import com.example.recyclerviewt12.MessageAdapter
import kotlinx.android.synthetic.main.activity_ranking_sexo.*
import java.text.DecimalFormat

class RankingPorSexoActivity : AppCompatActivity () {
private  var messages = mutableListOf<Message>()
    private var adapter =
        MessageAdapter(messages, this::onMessageItemClick )
    private var rankRepository: SQLiteRepository2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking_sexo)
        rankRepository = SQLiteRepository2(this)

        val spinner: Spinner = findViewById(R.id.sexos_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.sexos_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter

        }
        initRecyclerView()
        val filter = IntentFilter()

        filter.addAction("com.hello.action2")

        val updateUIReciver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                updateList()
            }
        }
        registerReceiver(updateUIReciver, filter)

    }
    private fun list(rank: MutableList<RankingSexo>){
        for(item in rank){
            val dec = DecimalFormat("#,###.##")
            val message = item.ranking?.let {
                item.nome?.let { it1 ->
                    item.frequencia?.let { it2 ->
                        Message(
                            it1,
                            it.toString()+" °",
                            dec.format(it2.toBigDecimal())
                        )
                    }
                }
            }

            if (message != null) {
                messages.add(message)
            }
            adapter.notifyItemInserted(messages.lastIndex)

        }
        Log.d("nome",rank.toString())
    }

    fun updateList() {
        rankRepository?.list { list(it) }
    }

    fun initRecyclerView(){
        rvMessages.adapter = adapter

        val layoutMAnager = GridLayoutManager(this, 2)

        layoutMAnager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if(position == 0) 2 else 1
            }
        }
        rvMessages.layoutManager = layoutMAnager

    }

    fun addMessage(view: View){
        val text : String = sexos_spinner.getSelectedItem().toString()
        Log.d("teste",text)

        val i = Intent(this, MyIntentService2::class.java)
        if(text == "Masculino"){
            i.putExtra("sexo", "M")
        } else {
            i.putExtra("sexo", "F")
        }
        startService(i)
//        messages.add(message)
//        adapter.notifyItemInserted(messages.lastIndex)
    }

    fun onMessageItemClick(message: Message){
        val s = "${message.title}\n${message.text}"
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }


}