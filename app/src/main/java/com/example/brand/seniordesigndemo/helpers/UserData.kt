package com.example.brand.seniordesigndemo.helpers

import java.io.Serializable

class UserData(deviceId: String) : Serializable {
    val deviceId = deviceId

    constructor(): this("")
}