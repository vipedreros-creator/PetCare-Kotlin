package service

import model.Box

class GestorBoxes {
    val boxes: List<Box> = List(10) { index -> Box() }
}