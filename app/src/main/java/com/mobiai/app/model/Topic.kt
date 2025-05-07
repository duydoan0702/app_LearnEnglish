package com.mobiai.app.model

data class Topic(val topicCode : String, val name : String , val numberLesson : Int
                    ,val levelCode:String, val urlImg : String){
    constructor() : this("","",0,"","")
}

