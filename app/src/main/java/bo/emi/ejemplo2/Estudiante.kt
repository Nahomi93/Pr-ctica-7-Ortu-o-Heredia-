package bo.emi.ejemplo2

import java.util.Date

data class Estudiante(val id: Long, val nombre: String, val apellido: String,
                      val fechaNacimiento: Date, val semestre: Int,
                      val idCiudad: Int, val ciudad: String, val direccion: String)