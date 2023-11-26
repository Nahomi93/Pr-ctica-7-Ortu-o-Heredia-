package bo.emi.ejemplo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar

class NuevoEstudiante : AppCompatActivity() {
    private lateinit var txtNombreEstudiante: EditText
    private lateinit var txtApellidoEstudiante: EditText
    private lateinit var txtFechaNacimientoEstudiante: EditText
    private lateinit var txtSemestreEstudiante: EditText
    private lateinit var txtIdCiudadEstudiante: EditText
    private lateinit var txtDireccionEstudiante: EditText
    private lateinit var btnEnviar: Button

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_estudiante)

        txtNombreEstudiante = findViewById(R.id.txtNombreEstudiante)
        txtApellidoEstudiante = findViewById(R.id.txtApellidoEstudiante)
        txtFechaNacimientoEstudiante = findViewById(R.id.txtFechaNacimientoEstudiante)
        txtSemestreEstudiante = findViewById(R.id.txtSemestreEstudiante)
        txtIdCiudadEstudiante = findViewById(R.id.txtIdCiudadEstudiante)
        txtDireccionEstudiante = findViewById(R.id.txtDireccionEstudiante)
        btnEnviar = findViewById(R.id.btnEnviar)

        btnEnviar.setOnClickListener {
            //val calendar = Calendar.getInstance()
            //calendar.set(txtFechaNacimientoEstudiante.year, txtFechaNacimientoEstudiante.month + 1, txtFechaNacimientoEstudiante.dayOfMonth)

            val sdf = SimpleDateFormat("yyyy-MM-dd")

            val estudiante = Estudiante(0L,
                txtNombreEstudiante.text.toString(),
                txtApellidoEstudiante.text.toString(),
                sdf.parse(txtFechaNacimientoEstudiante.text.toString()),
                Integer.parseInt(txtSemestreEstudiante.text.toString()),
                Integer.parseInt(txtIdCiudadEstudiante.text.toString()),
                "",
                txtDireccionEstudiante.text.toString()
            )
            run(getString(R.string.base_url) + "/estudiantes", estudiante)
            //run("http://192.168.1.65:8080" + "/estudiantes",estudiante)
            //run("http://192.168.137.250:8080" + "/estudiantes",estudiante)
        }
    }

    fun run(url: String, estudiante: Estudiante) {
        GlobalScope.launch {
            val obj = JSONObject()
            obj.put("nombre", estudiante.nombre)
            obj.put("apellido", estudiante.apellido)

            val sdf = SimpleDateFormat("yyyy-MM-dd")

            obj.put("fechaNacimiento", sdf.format(estudiante.fechaNacimiento))
            obj.put("semestre", estudiante.semestre)
            obj.put("idCiudad", estudiante.idCiudad)
            obj.put("direccion", estudiante.direccion)

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