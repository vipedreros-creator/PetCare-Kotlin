package service

import model.Paciente
import model.TipoDueno

class CalculadoraTarifa {

    companion object {
        const val IVA = 0.19
        const val DESCUENTO_MUNICIPAL = 0.50
    }

    fun calcularMontoFinal(paciente: Paciente, minutosUso: Int): Double {
        val costoBase = paciente.calcularCostoBase(minutosUso)
        val costoConIva = costoBase * (1 + IVA)

        return if (paciente.tipoDueno == TipoDueno.MUNICIPAL) {
            costoConIva * (1 - DESCUENTO_MUNICIPAL)
        } else {
            costoConIva
        }
    }
}