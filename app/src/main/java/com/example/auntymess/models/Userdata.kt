package com.example.auntymess.models

import android.net.Uri
import kotlin.properties.Delegates

class Userdata (
    var name: String="",
    var email: String="",
    var profileimage: String=""
){
    constructor(): this("","","")
//    constructor()
//
//    constructor(name: String,email: String,profileimage: String){
//        this.name=name
//        this.email=email
//        this.profileimage=profileimage
//    }
}


