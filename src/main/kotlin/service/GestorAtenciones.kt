package service

import kotlinx.coroutines.delay
import model.EstadoBox
import model.Exotico
import model.Paciente

class GestorAtenciones(
    private val gestorBoxes: GestorBoxes,
    private val validador: Validador,
    private val calculadora: CalculadoraTarifa,
    private val generadorTicket: GeneradorTicket,
    private val petCare: PetCare
) {

    /**
     * Registra el ingreso de un paciente al sistema.
     * Mientras espera confirmación del sensor (3 segundos), el box queda
     * en estado EnProceso indicando que se está registrando la entrada.
     */
    suspend fun registrarEntrada(paciente: Paciente) {
        if (!validador.validarCodigoAtencion(paciente.codigoAtencion)) {
            throw IllegalArgumentException("Código de atención inválido: '${paciente.codigoAtencion}'. Formato esperado: AA00AA.")
        }

        val box = gestorBoxes.buscarBoxLibre()
            ?: throw IllegalStateException("Sistema sin capacidad: no hay boxes libres en este momento.")

        box.estado = EstadoBox.EnProceso("Registrando entrada")
        val detalleSilvestre = if (paciente is Exotico) {
            if (paciente.esSilvestre) " (animal silvestre)" else " (no silvestre)"
        } else ""

        println("Box ${box.numero}: registrando entrada de ${paciente.nombre}$detalleSilvestre. Esperando confirmación del sensor...")
        delay(3000)

        box.estado = EstadoBox.EnAtencion(paciente)
        println("Box ${box.numero}: entrada confirmada. Paciente ${paciente.nombre} (${paciente.codigoAtencion}) en atención.")
    }

    /**
     * Registra la salida de un paciente, calcula la tarifa y emite el ticket.
     * Mientras procesa la salida (6,5 segundos), el box queda en estado
     * EnProceso indicando que se está calculando la tarifa.
     */
    suspend fun registrarSalida(codigoAtencion: String, minutosUso: Int) {
        val box = gestorBoxes.buscarBoxPorCodigoAtencion(codigoAtencion)
            ?: throw NoSuchElementException("No se encontró un paciente en atención con código '$codigoAtencion'.")

        val estadoActual = box.estado
        if (estadoActual !is EstadoBox.EnAtencion) {
            throw IllegalStateException("El box ${box.numero} no está actualmente en atención.")
        }
        val paciente = estadoActual.paciente

        box.estado = EstadoBox.EnProceso("Calculando tarifa")
        println("Box ${box.numero}: procesando salida de ${paciente.nombre}, calculando tarifa...")
        delay(6500)

        val monto = calculadora.calcularMontoFinal(paciente, minutosUso)

        if (!validador.validarMonto(monto, paciente, minutosUso)) {
            box.estado = EstadoBox.EnAtencion(paciente)
            throw IllegalStateException("Resultado de tarifa inválido ($monto) para el código '$codigoAtencion'.")
        }

        val ticket = generadorTicket.generarTicket(paciente, minutosUso, monto)
        petCare.registrarSalidaEnHistorial(ticket)
        box.estado = EstadoBox.Libre

        println("Box ${box.numero}: salida registrada. Ticket #${ticket.numero} - Monto: \$${"%,.0f".format(monto)}")
    }
}