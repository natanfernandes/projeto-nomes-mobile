package com.example.projeto_nomes

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.projeto_nomes.model.NomePorSexo
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import kotlinx.android.synthetic.main.activity_buscar_nomes_femininos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscarNomesFemininosActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_nomes_femininos)
    }

    fun getNome (view : View) {
        val nome = nome_feminino_value.text.toString()
        val call: Call<List<NomePorSexo>> =
            RetrofitInitializer().nomePorSexoService().buscarNomePorSexo(nome, "F")
        buscarAssincrono(call)
    }

    private fun exibirErro(t:Throwable){
        Toast.makeText(this, "CEP não encontrado", Toast.LENGTH_SHORT).show()
    }

    private fun preencherCampos(nome: List<NomePorSexo>?) {
        txtName.text = nome?.get(0)?.nome ?: ""
        txtLocalidade.text = nome?.get(0)?.localidade ?: ""
    }
    private fun buscarAssincrono(call: Call<List<NomePorSexo>>) {

        call.enqueue(object : Callback<List<NomePorSexo>> {

            override fun onResponse(call: Call<List<NomePorSexo>>, response: Response<List<NomePorSexo>>) {
                var nome: List<NomePorSexo>? = response?.body();
                Log.d("nome",nome?.get(0)?.res?.get(0)?.frequencia.toString())
                if (nome != null) {
                    populateGraphData(nome.get(0))
                }
                preencherCampos(nome)
            }

            override fun onFailure(call: Call<List<NomePorSexo>>, t: Throwable) {

                exibirErro(t)
            }
        })
    }
    fun populateGraphData(nome: NomePorSexo) {
        var xAxisValues = ArrayList<String>()
        var yValueGroup2 = ArrayList<BarEntry>()
        var aux : Float = 0f

        var barChartView = findViewById<BarChart>(R.id.chartConsumptionGraph)
        for (item in nome.res!!) {
            Log.d("teste",item.frequencia.toString())
            var str = item.periodo?.replace("[","")
            str = str?.replace("]","")
            if(str != null && str != "1930"){
                str = str?.drop(5)
                Log.d("teste",str)
            }
            if (str != null) {
                xAxisValues.add(str)
            }
            yValueGroup2.add(BarEntry(aux, item.frequencia!!.toFloat()))
            aux = aux+1f
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
        barDataSet2.setColor(Color.BLUE)

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