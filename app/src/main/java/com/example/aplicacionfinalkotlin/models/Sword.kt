package com.example.aplicacionfinalkotlin.models

class Sword {
    var materialName=""
    var dmg=0
    var swordName=""

    constructor() {}
    constructor(materialName:String, dmg: Int) {
        this.materialName = materialName
        this.dmg= dmg
        this.swordName= "Sword ("+ materialName + ")"
    }

}