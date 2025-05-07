package com.mobiai.app.model

data class Level(val levelCode : String, val name : String, val numberTopic : Int, val urlImg : String,
                 var lock:Boolean){
    constructor() : this("","",0,"",false)
}

