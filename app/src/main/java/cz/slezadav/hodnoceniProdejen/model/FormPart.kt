package cz.slezadav.hodnoceniProdejen.model

class FormPart(var title: String, var index: String) {


    var active = false
    var subparts = mutableListOf<Subpart>()

    class Subpart(var title: String, var index: String) {
        var score = 0f
    }
}