package service

import model.Felino
import model.Paciente
import model.TipoDueno

class Validador {

    // Formato: dos letras, dos dígitos, dos letras. Ejemplo: CA12CD.
    private val formatoCodigoAtencion = Regex("^[A-Za-z]{2}[0-9]{2}[A-Za-z]{2}$")

    fun validarCodigoAtencion(codigo: String): Boolean {
        return formatoCodigoAtencion.matches(codigo)
    }

    /**
     * Ninguna tarifa puede ser negativa ni igual a cero (R3), salvo el caso
     * de negocio legítimo del Felino atendido por menos de 20 minutos, cuyo
     * cobro es $0 por definición (R1) y no debe reportarse como error.
     */
    fun validarMonto(monto: Double, paciente: Paciente, minutosUso: Int): Boolean {
        if (paciente is Felino && minutosUso < Felino.MINUTOS_MINIMOS_COBRO) {
            return true
        }
        return monto > 0
    }

    /**
     * Convierte el texto ingresado por el usuario en un TipoDueno válido.
     * Si el valor no corresponde a ninguno de los tres definidos, retorna null.
     */
    fun parsearTipoDueno(valor: String): TipoDueno? {
        return try {
            TipoDueno.valueOf(valor.trim().uppercase())
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}