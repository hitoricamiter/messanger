package ru.zaikin.messanger

data class User(var name: String, var email: String, var id: String, var avatarMockUpResource: Int) {
    constructor() : this(name = "default", email = "default", id = "default", avatarMockUpResource = 0)
}