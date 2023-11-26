package bo.emi.ejemplo2

import java.util.Date

data class Docente (val id: Long, val nombre: String, val apellido: String,
                    val fechaNacimiento: Date, val antiguedad: Int,
                    val idCiudad: Int, val ciudad: String, val direccion: String)