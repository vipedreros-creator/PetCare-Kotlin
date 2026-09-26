package model

class Exotico : Paciente() {
    override fun calcularCostoBase(minutosUso: Int): Double {
        return 0.0
    }
}