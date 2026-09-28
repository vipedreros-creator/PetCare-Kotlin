package model

class Exotico(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    tipoDueno: TipoDueno,
    val esSilvestre: Boolean
) : Paciente(codigoAtencion, nombre, especie, tipoDueno) {

    companion object {
        const val TARIFA_BASE_HORA = 20000.0
        const val RECARGO_SILVESTRE = 0.30
    }

    override fun calcularCostoBase(minutosUso: Int): Double {
        val costoPorTiempo = TARIFA_BASE_HORA * (minutosUso / 60.0)
        return if (esSilvestre) {
            costoPorTiempo * (1 + RECARGO_SILVESTRE)
        } else {
            costoPorTiempo
        }
    }
}