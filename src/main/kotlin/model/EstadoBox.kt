package model

sealed class EstadoBox {

    /** El box está disponible y puede recibir un nuevo paciente. */
    object Libre : EstadoBox()

    /** El box tiene un paciente asignado actualmente. */
    data class EnAtencion(val paciente: Paciente) : EstadoBox()

    /** El box está siendo registrado (esperando al sensor). Se indica el motivo. */
    data class EnProceso(val motivo: String) : EstadoBox()

    /** El box está inhabilitado. Se registra el motivo. */
    data class FueraDeServicio(val motivo: String) : EstadoBox()
}