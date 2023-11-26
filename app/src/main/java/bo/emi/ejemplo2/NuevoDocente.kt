package bo.emi.ejemplo2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat

class NuevoDocente : AppCompatActivity() {
    private lateinit var txtNombreDocente: EditText
    private lateinit var txtApellidoDocente: EditText
    private lateinit var txtFechaNacimientoDocente: EditText
    private lateinit var txtAntiguedadDocente: EditText
    private lateinit var txtIdCiudadDocente: EditText
    private lateinit var txtDireccionDocente: EditText
    private lateinit var btnEnviar: Button

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_docente)

        txtNombreDocente = findViewById(R.id.txtNombreDocente)
        txtApellidoDocente = findViewById(R.id.txtApellidoDocente)
        txtFechaNacimientoDocente = findViewById(R.id.txtFechaNacimientoDocente)
        txtAntiguedadDocente = findViewById(R.id.txtAntiguedadDocente)
        txtIdCiudadDocente = findViewById(R.id.txtIdCiudadDocente)
        txtDireccionDocente = findViewById(R.id.txtDireccionDocente)
        btnEnviar = findViewById(R.id.btnEnviar)

        btnEnviar.setOnClickListener {
            //val calendar = Calendar.getInstance()
            //calendar.set(txtFechaNacimientoEstudiante.year, txtFechaNacimientoEstudiante.month + 1, txtFechaNacimientoEstudiante.dayOfMonth)

            val sdf = SimpleDateFormat("yyyy-MM-dd")

            val docente = Docente(0L,
                txtNombreDocente.text.toString(),
                txtApellidoDocente.text.toString(),
                sdf.parse(txtFechaNacimientoDocente.text.toString()),
                Integer.parseInt(txtAntiguedadDocente.text.toString()),
                Integer.parseInt(txtIdCiudadDocente.text.toString()),
                "",
                txtDireccionDocente.text.toString()
            )
            run(getString(R.string.base_url) + "/docentes", docente)
            //run("http://192.168.1.65:8080" + "/docentes",docente)
            //run("http://192.168.137.250:8080" + "/docentes",docente)
        }
    }

    fun run(url: String, docente: Docente) {
        GlobalScope.launch {
            val obj = JSONObject()
            obj.put("nombre", docente.nombre)
            obj.put("apellido", docente.apellido)

            val sdf = SimpleDateFormat("yyyy-MM-dd")

            obj.put("fechaNacimiento", sdf.format(docente.fechaNacimiento))
            obj.put("antiguedad", docente.antiguedad)
            obj.put("idCiudad", docente.idCiudad)
            obj.put("direccion", docente.direccion)

            val body = obj.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .post(body)
                .build()

            client.newCall(request).execute()

            runOnUiThread {
                finish()
            }
        }
    }
}