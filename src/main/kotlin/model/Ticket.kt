package model

class Ticket(
    val numero: Int,
    val tipoPaciente: String,
    val codigoAtencion: String,
    val tiempoUsoMinutos: Int,
    val montoPagado: Double,
    val tipoDueno: TipoDueno
)