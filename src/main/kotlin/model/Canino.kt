package model

class Canino : Paciente() {
    override fun calcularCostoBase(minutosUso: Int): Double {
        return 0.0
    }
}