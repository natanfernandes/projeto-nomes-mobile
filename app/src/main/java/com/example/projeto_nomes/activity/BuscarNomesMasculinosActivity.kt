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
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.activity.MyIntentService
import com.example.projeto_nomes.R
import com.example.projeto_nomes.model.NomePorSexo
import com.example.projeto_nomes.repository.SQLiteRepository
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import kotlinx.android.synthetic.main.activity_buscar_nomes_femininos.*
import kotlinx.android.synthetic.main.activity_buscar_nomes_femininos.txtLocalidade
import kotlinx.android.synthetic.main.activity_buscar_nomes_masculinos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscarNomesMasculinosActivity: AppCompatActivity(){
    private var nomesRepository: SQLiteRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_nomes_masculinos)
        nomesRepository = SQLiteRepository(this)
        val filter = IntentFilter()

        filter.addAction("com.hello.action")

        val updateUIReciver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                updateList()
            }
        }
        registerReceiver(updateUIReciver, filter)
    }

    fun getNomeMasculino (view : View) {
        val i = Intent(this, MyIntentService::class.java)
        val nome = nome_masculino_value.text.toString()
        i.putExtra("nome", nome)
        i.putExtra("sexo", "M")
        startService(i)
    }

    private fun list(nomes: NomePorSexo){
        if(nomes != null) {
            if (nomes.nome != null && nomes.resStr != null) {
                Log.d("nome",nomes?.resStr?.toString())
                populateGraphData(nomes)
            }
        }

    }

    fun updateList() {
        nomesRepository?.list { list(it[0]) }
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
        var barChartView = findViewById<BarChart>(com.example.projeto_nomes.R.id.graficoMasculino)
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


}