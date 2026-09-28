package service

class ReporteTurno(private val petCare: PetCare) {

    fun generarReporteCierre() {
        println()
        println("===== REPORTE DE CIERRE DE TURNO - ${petCare.nombreSistema} =====")

        if (petCare.historialAtenciones.isEmpty()) {
            println("No se registraron atenciones durante este turno.")
        } else {
            println(String.format("%-8s %-10s %-15s %-15s %-12s", "Ticket", "Tipo", "Código", "Tiempo(min)", "Monto"))
            for (ticket in petCare.historialAtenciones) {
                println(
                    String.format(
                        "%-8d %-10s %-15s %-15d \$%,.0f",
                        ticket.numero,
                        ticket.tipoPaciente,
                        ticket.codigoAtencion,
                        ticket.tiempoUsoMinutos,
                        ticket.montoPagado
                    )
                )
            }
        }

        println()
        println("--- Totales del turno ---")
        println("Total recaudado: \$${"%,.0f".format(petCare.recaudacionTotal())}")
        println("Pacientes atendidos: ${petCare.historialAtenciones.size}")
        println("Ingreso promedio por paciente: \$${"%,.0f".format(petCare.ingresoPromedio())}")
        println("Tipo de paciente que más ingresos generó: ${petCare.tipoConMasIngresos() ?: "N/A"}")
        println("Boxes disponibles al cierre: ${petCare.boxesDisponibles()}")
    }
}