package com.example.aplicacionfinalkotlin.models

import android.content.Context
import com.example.aplicacionfinalkotlin.R

class Material {
    lateinit var context: Context
    var type=-1
    var name=""
    var dmg=0

    constructor() {}

    constructor(type: Int, context: Context) {
        this.name = name
        this.context=context
        when (type) {
            1-> this.name= context.resources.getString(R.string.stone)
            2-> this.name= context.resources.getString(R.string.iron)
            3-> this.name= context.resources.getString(R.string.dragonScale)
        }

        when (name) {
            context.resources.getString(R.string.stone) -> this.dmg=1
            context.resources.getString(R.string.iron) -> this.dmg=2
            context.resources.getString(R.string.dragonScale) -> this.dmg=3
        }
    }
}