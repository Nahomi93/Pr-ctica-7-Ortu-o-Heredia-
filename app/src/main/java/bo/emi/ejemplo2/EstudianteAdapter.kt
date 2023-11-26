package bo.emi.ejemplo2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class EstudianteAdapter(context: Context, estudiantes: List<Estudiante>) :
    ArrayAdapter<Estudiante>(context, 0, estudiantes) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val estudiante: Estudiante? = getItem(position)

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.estudiante_item, parent, false)
        }

        //val imgEstudiante = convertView!!.findViewById<View>(R.id.imgEstudiante) as ImageView
        val txtNombre = convertView!!.findViewById<View>(R.id.txtItemNombreEstudiante) as TextView
        val txtApellido = convertView!!.findViewById<View>(R.id.txtItemApellidoEstudiante) as TextView
        val txtCiudad = convertView!!.findViewById<View>(R.id.txtItemCiudadEstudiante) as TextView
        val txtSemestre = convertView!!.findViewById<View>(R.id.txtItemSemestreEstudiante) as TextView

        //imgEstudiante.setImageResource(R.mipmap.ic_launcher_round)
        txtNombre.setText(estudiante?.nombre)
        txtApellido.setText(estudiante?.apellido)
        txtCiudad.setText(estudiante?.ciudad)
        txtSemestre.setText(estudiante?.semestre.toString())

        return convertView
    }
}