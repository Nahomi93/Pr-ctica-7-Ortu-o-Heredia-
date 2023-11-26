package bo.emi.ejemplo2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONTokener
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private lateinit var btnNuevoEstudiante: Button
    private lateinit var btnNuevoDocente: Button
    private lateinit var btnMostrarEstudiante: Button
    private lateinit var btnMostrarDocente: Button
    private lateinit var lstListado: ListView
    private lateinit var textView: TextView

    //private lateinit var lstListadoDocente: ListView
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNuevoEstudiante = findViewById(R.id.btnNuevoEstudiante)
        btnNuevoDocente = findViewById(R.id.btnNuevoDocente)
        btnMostrarEstudiante = findViewById(R.id.btnMostrarEstudiante)
        btnMostrarDocente = findViewById(R.id.btnMostrarDocente)
        lstListado = findViewById(R.id.lstListado)
        textView = findViewById(R.id.textView)

        val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            run(getString(R.string.base_url) + "/estudiantes")
            //run("http://192.168.137.250:8080" + "/estudiantes")
        }
        val resultNuevoDocente = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
           // run("http://192.168.137.250:8080" + "/docentes")
            run(getString(R.string.base_url) + "/docentes")
        }
        btnNuevoEstudiante.setOnClickListener {
            val intent = Intent(applicationContext, NuevoEstudiante::class.java)
            result.launch(intent)
        }
        btnNuevoDocente.setOnClickListener {
            val intent = Intent(applicationContext, NuevoDocente::class.java)
            resultNuevoDocente.launch(intent)
        }
        btnMostrarEstudiante.setOnClickListener {
            //run("http://192.168.137.250:8080" + "/estudiantes")
            run(getString(R.string.base_url) + "/estudiantes")
        }
        btnMostrarDocente.setOnClickListener {
            //run("http://192.168.137.250:8080" + "/docentes")
            run(getString(R.string.base_url) + "/docentes")

        }
    }

    fun run(url: String) {
        GlobalScope.launch {
            val request = Request.Builder().url(url).get().build()
            val response = client.newCall(request).execute()

            val persona = JSONTokener(response.body!!.string()).nextValue() as JSONArray

            val listaEstudiantes = arrayListOf<Estudiante>()
            val listaDocentes = arrayListOf<Docente>()
            for (i in 0 until persona.length()) {
                val estudianteJson = persona.getJSONObject(i)
                val formatter = SimpleDateFormat("yyyy-MM-dd")

                if (estudianteJson.has("antiguedad")) {
                    val docente = Docente(
                        estudianteJson.getLong("id"),
                        estudianteJson.getString("nombre"),
                        estudianteJson.getString("apellido"),
                        formatter.parse(estudianteJson.getString("fechaNacimiento")),
                        estudianteJson.getInt("antiguedad"), // Agregar la propiedad "antiguedad"
                        estudianteJson.getInt(("idCiudad")),
                        estudianteJson.getString("ciudad"),
                        estudianteJson.getString("direccion")
                    )
                    listaDocentes.add(docente)
                }else {
                    val estudiante = Estudiante(
                        estudianteJson.getLong("id"),
                        estudianteJson.getString("nombre"),
                        estudianteJson.getString("apellido"),
                        formatter.parse(estudianteJson.getString("fechaNacimiento")),
                        //estudianteJson.getInt("semestre"),
                        if (estudianteJson.has("semestre")) estudianteJson.getInt("semestre") else 0,
                        estudianteJson.getInt(("idCiudad")),
                        estudianteJson.getString("ciudad"),
                        estudianteJson.getString("direccion")
                    )

                    listaEstudiantes.add(estudiante)
                }
            }
            if(listaEstudiantes.isEmpty()) {
                println("entra a la lista docentes")

                runOnUiThread {
                    textView.text="LISTA DOCENTES"
                    lstListado.adapter = DocenteAdapter(applicationContext,listaDocentes )
                }
            }
            if(listaDocentes.isEmpty()){
                println("entra a la lista estudiantes")
                runOnUiThread {
                    textView.text="LISTA ESTUDIANTES"
                    lstListado.adapter = EstudianteAdapter(applicationContext, listaEstudiantes)
                    //lstListadoDocente.adapter = DocenteAdapter(applicationContext,listaDocentes )

                }
            }

        }

    }
}