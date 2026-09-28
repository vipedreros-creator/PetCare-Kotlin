package service

import model.Box
import model.EstadoBox

class GestorBoxes(cantidadBoxes: Int) {

    val boxes: List<Box> = List(cantidadBoxes) { index -> Box(index + 1) }

    /** Busca el primer box en estado Libre disponible. */
    fun buscarBoxLibre(): Box? = boxes.firstOrNull { it.estado is EstadoBox.Libre }

    /** Busca el box que tiene en atención al paciente con el código dado. */
    fun buscarBoxPorCodigoAtencion(codigo: String): Box? = boxes.firstOrNull { box ->
        val estadoActual = box.estado
        estadoActual is EstadoBox.EnAtencion && estadoActual.paciente.codigoAtencion == codigo
    }

    fun contarBoxesLibres(): Int = boxes.count { it.estado is EstadoBox.Libre }
}