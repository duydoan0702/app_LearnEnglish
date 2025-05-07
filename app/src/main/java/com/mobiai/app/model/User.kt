package com.mobiai.app.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(var email:String,
var name:String,
var pass:String,
var urlImage:String?=null,
var ruby:Long = 0,
var totalXp :Long = 0,
var role:Boolean = false){
    constructor() : this("","","","",0,0,false
    )
}
