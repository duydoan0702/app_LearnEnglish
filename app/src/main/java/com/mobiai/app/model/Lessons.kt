package com.mobiai.app.model

data class Lessons(val lessonCode : String ="", val level : Int=0 , val topicCode : String="")
{
    constructor() : this("",0,"")


}
