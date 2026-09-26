package model

class Felino : Paciente() {
    override fun calcularCostoBase(minutosUso: Int): Double {
        return 0.0
    }
}