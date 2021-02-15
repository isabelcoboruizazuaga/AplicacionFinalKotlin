package com.example.aplicacionfinalkotlin.models

import java.io.Serializable

class Message : Serializable, Comparable<Message> {
    var transmitter: String? = null
    var receiver: String? = null
    var messageBody: String? = null
    var transmitterName: String? = null
    var date: Long? = null

    constructor() {}
    constructor(transmitter: String?, receiver: String?, messageBody: String?, transmitterName: String?) {
        this.transmitter = transmitter
        this.receiver = receiver
        this.messageBody = messageBody
        this.transmitterName = transmitterName
        date = System.currentTimeMillis()
    }

    override fun compareTo(o: Message): Int {
        return if (date == null || o.date == null) {
            0
        } else date!!.compareTo(o.date!!)
    }
}