package ru.zaikin.messanger

data class AwesomeMessage(var text: String, var name: String, var imageUrl: String?) {

    constructor() : this(text = "", name = "", imageUrl = "")


}