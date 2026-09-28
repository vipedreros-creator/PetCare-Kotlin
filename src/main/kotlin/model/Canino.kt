package model

class Canino(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    tipoDueno: TipoDueno
) : Paciente(codigoAtencion, nombre, especie, tipoDueno) {

    companion object {
        const val TARIFA_BASE_HORA = 12000.0
        const val DESCUENTO_CONVENIO = 0.20
    }

    override fun calcularCostoBase(minutosUso: Int): Double {
        val costoPorTiempo = TARIFA_BASE_HORA * (minutosUso / 60.0)
        return if (tipoDueno == TipoDueno.CONVENIO) {
            costoPorTiempo * (1 - DESCUENTO_CONVENIO)
        } else {
            costoPorTiempo
        }
    }
}