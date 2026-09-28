import kotlinx.coroutines.runBlocking
import model.Canino
import model.Exotico
import model.Felino
import model.Paciente
import service.PetCare
import service.ReporteTurno

fun main() = runBlocking {
    val petCare = PetCare()
    val reporte = ReporteTurno(petCare)

    println("========================================")
    println(" Bienvenido a ${petCare.nombreSistema}")
    println(" Sistema de Gestión de Boxes Veterinarios")
    println(" Capacidad: ${petCare.capacidadBoxes} boxes")
    println("========================================")

    var continuar = true
    while (continuar) {
        mostrarMenu()
        when (readLine()?.trim()) {
            "1" -> registrarEntradaDesdeConsola(petCare)
            "2" -> registrarSalidaDesdeConsola(petCare)
            "3" -> mostrarConsultas(petCare)
            "4" -> reporte.generarReporteCierre()
            "5" -> {
                println("Cerrando turno de ${petCare.nombreSistema}. ¡Hasta pronto!")
                continuar = false
            }
            else -> println("Opción inválida. Intente nuevamente.")
        }
    }
}

private fun mostrarMenu() {
    println(
        """
        |
        |----- Menú Principal -----
        |1. Registrar entrada de paciente
        |2. Registrar salida de paciente
        |3. Consultas de negocio
        |4. Generar reporte de cierre de turno
        |5. Salir
        |Seleccione una opción:
        """.trimMargin()
    )
}

private suspend fun registrarEntradaDesdeConsola(petCare: PetCare) {
    try {
        print("Tipo de paciente (Canino/Felino/Exotico): ")
        val tipo = readLine()?.trim()?.uppercase() ?: ""

        print("Código de atención (formato AA00AA): ")
        val codigo = readLine()?.trim() ?: ""

        print("Nombre de la mascota: ")
        val nombre = readLine()?.trim() ?: ""

        print("Especie: ")
        val especie = readLine()?.trim() ?: ""

        print("Tipo de dueño (PARTICULAR/CONVENIO/MUNICIPAL): ")
        val tipoDuenoTexto = readLine()?.trim() ?: ""
        val tipoDueno = petCare.validador.parsearTipoDueno(tipoDuenoTexto)
            ?: throw IllegalArgumentException("Tipo de dueño inválido: '$tipoDuenoTexto'.")

        val paciente: Paciente = when (tipo) {
            "CANINO" -> Canino(codigo, nombre, especie, tipoDueno)
            "FELINO" -> Felino(codigo, nombre, especie, tipoDueno)
            "EXOTICO" -> {
                print("¿Es un animal silvestre? (si/no): ")
                val esSilvestre = readLine()?.trim()?.equals("si", ignoreCase = true) == true
                Exotico(codigo, nombre, especie, tipoDueno, esSilvestre)
            }
            else -> throw IllegalArgumentException("Tipo de paciente inválido: '$tipo'.")
        }

        petCare.gestorAtenciones.registrarEntrada(paciente)

    } catch (e: Exception) {
        println("⚠ No fue posible registrar la entrada: ${e.message}")
    }
}

private suspend fun registrarSalidaDesdeConsola(petCare: PetCare) {
    try {
        print("Código de atención del paciente que sale: ")
        val codigo = readLine()?.trim() ?: ""

        print("Tiempo de uso en minutos: ")
        val minutos = readLine()?.trim()?.toIntOrNull()
            ?: throw IllegalArgumentException("El tiempo de uso debe ser un número entero válido.")

        if (minutos < 0) {
            throw IllegalArgumentException("El tiempo de uso no puede ser negativo.")
        }

        petCare.gestorAtenciones.registrarSalida(codigo, minutos)

    } catch (e: Exception) {
        println("⚠ No fue posible registrar la salida: ${e.message}")
    }
}

private fun mostrarConsultas(petCare: PetCare) {
    println()
    println("--- Consultas de negocio ---")
    println("Boxes disponibles: ${petCare.boxesDisponibles()}")
    println("Códigos de pacientes convenio atendidos: ${petCare.pacientesConvenio().map { it.codigoAtencion }}")
    println("Ingreso promedio por paciente atendido: \$${"%,.0f".format(petCare.ingresoPromedio())}")
    println("Códigos de pacientes finalizados en el turno: ${petCare.codigosFinalizados()}")

    val masTiempo = petCare.pacienteConMasTiempo()
    if (masTiempo != null) {
        println("Paciente con más tiempo de uso: ${masTiempo.codigoAtencion} (${masTiempo.tiempoUsoMinutos} min)")
    } else {
        println("Paciente con más tiempo de uso: sin datos aún.")
    }
}