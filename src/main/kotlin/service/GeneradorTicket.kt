package service

import model.Canino
import model.Exotico
import model.Felino
import model.Paciente
import model.Ticket

class GeneradorTicket {

    private var contador = 0

    fun generarTicket(paciente: Paciente, minutosUso: Int, monto: Double): Ticket {
        contador++
        val tipo = when (paciente) {
            is Canino -> "Canino"
            is Felino -> "Felino"
            is Exotico -> "Exotico"
            else -> "Desconocido"
        }
        return Ticket(
            numero = contador,
            tipoPaciente = tipo,
            codigoAtencion = paciente.codigoAtencion,
            tiempoUsoMinutos = minutosUso,
            montoPagado = monto,
            tipoDueno = paciente.tipoDueno
        )
    }
}