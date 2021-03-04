package com.example.aplicacionfinalkotlin.models

import java.util.ArrayList

class User {
    var hasSaves=false
    var woods=0
    var traps=0
    var meat=0
    var sword: Sword? = null
    var key= false
    var ring=0
    var healthPotion=0
    var staminaPotion=0

    var uid: String? = null
    var email: String? = null
    var name: String? = null
    var phone: String? = null
    var provider: String? = null
    var receivedList: MutableList<Message> = ArrayList()
    var sentList: MutableList<Message> = ArrayList()

    constructor() {}
    constructor(uid: String?, email: String?) {
        this.uid = uid
        this.email = email
    }

    constructor(uid: String?, email: String?, name: String?, phone: String?, provider: String?) {
        this.uid = uid
        this.email = email
        this.name = name
        this.phone = phone
        this.provider = provider
    }


}