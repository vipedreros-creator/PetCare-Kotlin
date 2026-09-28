package model

class Felino(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    tipoDueno: TipoDueno
) : Paciente(codigoAtencion, nombre, especie, tipoDueno) {

    companion object {
        const val TARIFA_BASE_HORA = 9000.0
        const val MINUTOS_MINIMOS_COBRO = 20
    }

    override fun calcularCostoBase(minutosUso: Int): Double {
        if (minutosUso < MINUTOS_MINIMOS_COBRO) return 0.0
        return TARIFA_BASE_HORA * (minutosUso / 60.0)
    }
}