package com.mobiai.app.model

 class Vocabulary {
    data class Sentence_Structure(val nameStructure:String,val structure:String, val example:String, val explain:String){
        constructor() : this("","","","")
    }
    data class Word_Types(val nameWord:String, val describe:String, val example:String){
        constructor() : this("","","")

    }
}
