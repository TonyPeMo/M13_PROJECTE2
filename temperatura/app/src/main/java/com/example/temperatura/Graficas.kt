package com.example.temperatura

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.echo.holographlibrary.Line
import com.echo.holographlibrary.LineGraph
import com.echo.holographlibrary.LineGraph.OnPointClickedListener
import com.echo.holographlibrary.LinePoint
import com.echo.holographlibrary.PieGraph
import com.echo.holographlibrary.PieSlice
import com.example.temperatura.data.RegistroLine
import com.example.temperatura.data.Registro
import com.example.temperatura.databinding.ActivityGraficasBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.random.Random
import kotlin.collections.ArrayList

class Graficas : AppCompatActivity() {

    private lateinit var selectedDateTextView_inicio: TextView
    private lateinit var selectedTimeTextView_inicio: TextView
    private lateinit var selectedDateTextView_final: TextView
    private lateinit var selectedTimeTextView_final: TextView

    private lateinit var selectedDateInicio: Date
    private lateinit var selectedTimeInicio: Date
    private lateinit var selectedDateFinal: Date
    private lateinit var selectedTimeFinal: Date

    //Graficas View
    private var cantidadPuntos : Int = 8
    private  lateinit var binding : ActivityGraficasBinding
    //private  lateinit var pieGrafica: PieGraph
    private  lateinit var lineGrafica: LineGraph
    //private var listaRegistrosPastel: ArrayList<Registro> = ArrayList()
    private var listaRegistrosLine: ArrayList<RegistroLine> = ArrayList()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_graficas)

        //Graficos
        binding = ActivityGraficasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvPuntos.text = "Días\n"

        /*
        linea = datosGrafica(linea, 15.0, 18.5)
        linea = datosGrafica(linea, 16.0, 20.0)
        linea = datosGrafica(linea, 17.0, 19.5)
        linea = datosGrafica(linea, 18.0, 20.7)
        linea.color = Color.parseColor("#FFBB33");
*/
        // Genera puntos para la línea
        var linea = Line()
        for (i in 1..cantidadPuntos) {
            var ejeX = 15.0 + i
            val ejeY = String.format("%.1f", Random.nextDouble(15.5, 24.8)).toDouble()
            linea = datosGrafica(linea, ejeX, ejeY, true)
        }
        linea.color = Color.parseColor("#FFBB33")

        graficarL(linea)


        // Segunda linea

        var segundaLinea = Line()
        for (i in 1..cantidadPuntos) {
            var ejeX = 15.0 + i
            val ejeY = 18.0
            segundaLinea = datosGrafica(segundaLinea, ejeX, ejeY, false)
        }
        segundaLinea.color = Color.parseColor("#1C3AFF") // Cambia el color si es necesario
        graficarL(segundaLinea)

        // Tercera linea

        var terceraLinea = Line()
        for (i in 1..cantidadPuntos) {
            var ejeX = 15.0 + i
            val ejeY = 23.0
            terceraLinea = datosGrafica(terceraLinea, ejeX, ejeY, false)
        }
        terceraLinea.color = Color.parseColor("#ff0000") // Cambia el color si es necesario
        graficarL(terceraLinea)


        binding.graphLine.setOnPointClickedListener { lineIndex, pointIndex ->
            Toast.makeText(
                this@Graficas,
                "Linea: $lineIndex, Punto: $pointIndex",
                Toast.LENGTH_LONG
            ).show()
        }

        /*
        // GRAFICAS PASTEL
            pieGrafica = findViewById(R.id.graphPie) as PieGraph
            binding.btnGuardar.setOnClickListener{
                listaRegistrosPastel.add(Registro("reg1", 18.50f, "#006400"))
                listaRegistrosPastel.add(Registro("reg2", 24.50f, "#228B22"))
                listaRegistrosPastel.add(Registro("reg3", 16.50f, "#808000"))
                listaRegistrosPastel.add(Registro("reg4", 20.50f, "#00FF00"))
                listaRegistrosPastel.add(Registro("reg5", 21.10f, "#7FFF00"))
                listaRegistrosPastel.add(Registro("reg6", 18.70f, "#00FF7F"))
            }

            binding.btnGenerar.setOnClickListener { graficarPie() }
           */


        // GRAFICAS LINEA
            /*lineGrafica = findViewById(R.id.graphLine) as LineGraph

            binding.btnGuardar.setOnClickListener{

                listaRegistrosLine.add(RegistroLine(20.50f,1))
                listaRegistrosLine.add(RegistroLine(22.50f,2))
                listaRegistrosLine.add(RegistroLine(21.50f,3))
            }
            binding.btnGenerar.setOnClickListener { graficarLine() }
            fun datosGrafica(linea: Line, ejeX: Double, ejeY: Double): Line {
                val punto = LinePoint()
                punto.setX(ejeX)
                punto.setY(ejeY)
                linea.addPoint(punto)
                binding.tvPuntos.text = "${binding.tvPuntos.text}\nX: $ejeX, Y:$ejeY"
                return linea
            }

            fun graficar(linea: Line) {
                binding.lineGrafica.addLine(linea)
                binding.lineGrafica.setRangeX(1f, 4f)
                binding.lineGrafica.setRangeY(0f, 10f)
                binding.lineGrafica.lineToFill = 0
            }
            */

        // FECHA Inicio
            selectedDateTextView_inicio = findViewById(R.id.textViewDatePicker_inicio)
            selectedTimeTextView_inicio = findViewById(R.id.textViewTimePicker_inicio)

            // FECHA Final
            selectedDateTextView_final = findViewById(R.id.textViewDatePicker_final)
            selectedTimeTextView_final = findViewById(R.id.textViewTimePicker_final)

            // Establecer la fecha y hora por defecto para el inicio y el final
            val currentDate = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            // Solo establecer la fecha y la hora por defecto en la fecha de inicio
            selectedDateTextView_inicio.text = "Select date"
            selectedTimeTextView_inicio.text = "08:00"

            // Establecer la fecha y la hora por defecto en la fecha final
            selectedDateTextView_final.text = dateFormat.format(currentDate.time)
            selectedTimeTextView_final.text = timeFormat.format(currentDate.time)

            selectedDateInicio = Date()
            selectedTimeInicio = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 8); set(Calendar.MINUTE, 0) }.time

            selectedDateFinal = currentDate.time
            selectedTimeFinal = currentDate.time
    }

    // Función para agregar puntos a la línea
    private fun datosGrafica(linea: Line, ejeX: Double, ejeY: Double, texto: Boolean) : Line {
        val punto = LinePoint()
        punto.setX(ejeX)
        punto.setY(ejeY)
        linea.addPoint(punto)

        if (texto) {
            binding.tvPuntos.text = "${binding.tvPuntos.text}\n15/05/2024 - Temperatura: $ejeY º"
        }
        return(linea)
    }

    fun graficarL(linea: Line) {
        binding.graphLine.addLine(linea)
        binding.graphLine.setRangeX(15f, 23f)
        binding.graphLine.setRangeY(15f,26f)
    }

    // GRAFICA PASTEL

    /*fun graficarPie (){
        for (i in 0 until listaRegistrosPastel.size) {
            val rebanada = PieSlice()
            rebanada.color = Color.parseColor(listaRegistrosPastel[i].color)
            rebanada.value = listaRegistrosPastel[i].temperatura.toString().toFloat()
            pieGrafica.addSlice(rebanada)
        }
    }*/
    fun showDatePicker(view: View) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        datePicker.addOnPositiveButtonClickListener {
            // Handle positive button click and get selected date
            val selectedDateInMillis = it
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selectedDateInMillis
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)
            if (view.id == R.id.textViewDatePicker_inicio) {
                selectedDateTextView_inicio.text = formattedDate
                selectedDateInicio = calendar.time
            } else {
                selectedDateTextView_final.text = formattedDate
                selectedDateFinal = calendar.time
            }
            // Do something with the selected date
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun showTimePicker(view: View) {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select time")
                .build()

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val selectedTime = timeFormat.parse(String.format("%02d:%02d", hour, minute))!!
            if (view.id == R.id.textViewTimePicker_inicio) {
                selectedTimeTextView_inicio.text = String.format("%02d:%02d", hour, minute)
                selectedTimeInicio = selectedTime
            } else {
                selectedTimeTextView_final.text = String.format("%02d:%02d", hour, minute)
                selectedTimeFinal = selectedTime
            }
            // Do something with the selected time
        }

        timePicker.show(supportFragmentManager, "timePicker")
    }


   /*
    fun cambiarGrafico(view: View) {
        if (pieGrafica.visibility == View.VISIBLE) {
            pieGrafica.visibility = View.GONE
            lineGrafica.visibility = View.VISIBLE
            // Llama a la función para generar el gráfico de líneas aquí si es necesario
        } else {
            pieGrafica.visibility = View.VISIBLE
            lineGrafica.visibility = View.GONE
        }
    }
*/

    fun toAtras(view: View) {
        onBackPressed()
    }
}
