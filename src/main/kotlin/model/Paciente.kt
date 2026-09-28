package model

import java.time.LocalDateTime

open class Paciente(
    val codigoAtencion: String,
    val nombre: String,
    val especie: String,
    val tipoDueno: TipoDueno,
    val fechaHoraIngreso: LocalDateTime = LocalDateTime.now()
) {
    open fun calcularCostoBase(minutosUso: Int): Double {
        return 0.0
    }
}