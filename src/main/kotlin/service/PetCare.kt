package service

import model.Ticket
import model.TipoDueno

class PetCare {

    val nombreSistema = "PetCare"
    val capacidadBoxes = 10

    val gestorBoxes = GestorBoxes(capacidadBoxes)
    val validador = Validador()
    val calculadora = CalculadoraTarifa()
    val generadorTicket = GeneradorTicket()
    val gestorAtenciones = GestorAtenciones(gestorBoxes, validador, calculadora, generadorTicket, this)

    val historialAtenciones: MutableList<Ticket> = mutableListOf()

    /** Llamado por GestorAtenciones al completar una salida exitosa. */
    fun registrarSalidaEnHistorial(ticket: Ticket) {
        historialAtenciones.add(ticket)
    }

    // ---------- Consultas de negocio (R4) ----------

    fun boxesDisponibles(): Int = gestorBoxes.contarBoxesLibres()

    fun pacientesConvenio(): List<Ticket> =
        historialAtenciones.filter { it.tipoDueno == TipoDueno.CONVENIO }

    fun recaudacionTotal(): Double =
        historialAtenciones.sumOf { it.montoPagado }

    fun recaudacionPorTipo(): Map<String, Double> =
        historialAtenciones
            .groupBy { it.tipoPaciente }
            .mapValues { (_, tickets) -> tickets.sumOf { it.montoPagado } }

    fun ingresoPromedio(): Double =
        if (historialAtenciones.isEmpty()) 0.0 else recaudacionTotal() / historialAtenciones.size

    fun codigosFinalizados(): List<String> =
        historialAtenciones.map { it.codigoAtencion }

    fun pacienteConMasTiempo(): Ticket? =
        historialAtenciones.maxByOrNull { it.tiempoUsoMinutos }

    fun tipoConMasIngresos(): String? =
        recaudacionPorTipo().maxByOrNull { it.value }?.key
}