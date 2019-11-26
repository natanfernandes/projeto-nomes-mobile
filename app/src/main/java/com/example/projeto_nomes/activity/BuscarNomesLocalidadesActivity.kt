package com.example.projeto_nomes.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.android.activity.MyIntentService
import com.android.activity.MyIntentService3
import com.android.activity.MyIntentService4
import com.example.projeto_nomes.model.Localidade
import com.example.projeto_nomes.model.NomePorSexo
import com.example.projeto_nomes.repository.SQLiteRepository3
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import kotlinx.android.synthetic.main.activity_buscar_nomes_femininos.txtLocalidade
import kotlinx.android.synthetic.main.activity_buscar_nomes_masculinos.*
import com.example.projeto_nomes.repository.SQLiteRepository
import kotlinx.android.synthetic.main.activity_buscar_nomes_localidades.*


class BuscarNomesLocalidadesActivity: AppCompatActivity(){
    private var localidadeRepository: SQLiteRepository3? = null
    private var nomesRepository: SQLiteRepository? = null
    var updateUIReciver : BroadcastReceiver? = null
    var updateUIReciver2 : BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.projeto_nomes.R.layout.activity_buscar_nomes_localidades)

        localidadeRepository = SQLiteRepository3(this)
        nomesRepository = SQLiteRepository(this)
        val filter = IntentFilter()
        filter.addAction("getLocalidades")
        val filter2 = IntentFilter()
        filter2.addAction("getNome")



        updateUIReciver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                updateList()
            }
        }
        updateUIReciver2 = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                updateNome()
            }
        }
        registerReceiver(updateUIReciver, filter)
        registerReceiver(updateUIReciver2, filter2)
        val i = Intent(this, MyIntentService3::class.java)
        startService(i)
    }

    fun getNomeLocalidade (view : View) {
        var text : String = localidades_spinner.getSelectedItem().toString()
        val i = Intent(this, MyIntentService4::class.java)
        val text_id = text.split(" - ")[0]
        val nome = nome_localidade_value.text.toString()
        i.putExtra("nome", nome)
        i.putExtra("sexo", "M")
        i.putExtra("localidade", text_id)
        i.putExtra("act",1)
        startService(i)
    }

    private fun list(nomes: MutableList<Localidade>){
        val mylist = ArrayList<String>()
        for(item in nomes) {
            Log.d("teste",item.toString())
            mylist.add(item.id_local + " - " + item.nome)
        }
        val spinner: Spinner = findViewById(com.example.projeto_nomes.R.id.localidades_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        var adapter  = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    fun updateList() {
        localidadeRepository?.list { list(it) }
    }

    fun updateNome() {
        nomesRepository?.list { listNome(it[0]) }
    }
    private fun listNome(nomes: NomePorSexo){
        if(nomes != null) {
            val teste = nomes
            if (teste.nome != null && teste.resStr != null) {
                Log.d("nome",teste?.resStr?.toString())
                populateGraphData(teste)
            }
        }

    }


    private fun exibirErro(t:Throwable){
        Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
    }

    private fun preencherCampos(nome: List<NomePorSexo>?) {
        txtNameM.text = nome?.get(0)?.nome ?: ""
        txtLocalidade.text = nome?.get(0)?.localidade ?: ""
    }

    fun populateGraphData(nome: NomePorSexo) {
        var xAxisValues = ArrayList<String>()
        var yValueGroup2 = ArrayList<BarEntry>()
        var aux : Float = 0f
        var barChartView = findViewById<BarChart>(com.example.projeto_nomes.R.id.graficoLocalidades)
        var freqAux = nome.frequencia?.split(",")
        var resAux = nome.resStr?.split(",")

        if (resAux != null && freqAux != null ) {
            for (i in resAux.indices) {
                if(resAux[i] != "" && freqAux[i] != ""){
                    xAxisValues.add(resAux[i])
                    yValueGroup2.add(BarEntry(aux, freqAux[i].toFloat()))
                    aux = aux+1f
                }
            }
        }
        val barWidth: Float
        val barSpace: Float
        val groupSpace: Float
        val groupCount = 9

        barWidth = 0.25f
        barSpace = 0.27f
        groupSpace = 1f

        // draw the graph
        var barDataSet2: BarDataSet


        barDataSet2 = BarDataSet(yValueGroup2, "")
        barDataSet2.setColor(Color.MAGENTA)

        barDataSet2.setDrawIcons(false)
        barDataSet2.setDrawValues(false)

        var barData = BarData(barDataSet2)

        barChartView.description.isEnabled = false
        barData.setValueFormatter(LargeValueFormatter())
        barChartView.setData(barData)
        barChartView.getBarData().setBarWidth(barWidth)
        barChartView.getXAxis().setAxisMinimum(0f)
        barChartView.getXAxis().setAxisMaximum(9f)
        barChartView.setFitBars(true)
        barChartView.getData().setHighlightEnabled(false)
        barChartView.invalidate()


        val xAxis = barChartView.getXAxis()
        xAxis.setGranularity(1f)
        xAxis.setGranularityEnabled(true)
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 10f

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setValueFormatter(IndexAxisValueFormatter(xAxisValues))

        xAxis.setLabelCount(9)
        xAxis.mAxisMaximum = 9f
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 7f
        xAxis.spaceMax = 7f

        barChartView.setVisibleXRangeMaximum(10f)
        barChartView.setVisibleXRangeMinimum(9f)
        barChartView.setDragEnabled(true)

        //Y-axis
        barChartView.getAxisRight().setEnabled(false)
        barChartView.setScaleEnabled(true)

        val leftAxis = barChartView.getAxisLeft()
        leftAxis.setValueFormatter(LargeValueFormatter())
        leftAxis.setDrawGridLines(false)
        leftAxis.setSpaceTop(1f)
        leftAxis.setAxisMinimum(0f)


        barChartView.data = barData
        barChartView.setVisibleXRange(1f, 9f)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(updateUIReciver)
        unregisterReceiver(updateUIReciver2)
    }
}