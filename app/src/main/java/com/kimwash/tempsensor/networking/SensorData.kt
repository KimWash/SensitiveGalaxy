package com.kimwash.tempsensor.networking

import java.util.*

class SensorData(var temperature: Float, var humidity: Float, var light:Float, var time: Date) {
}

class ResponseData(var success:Boolean){
}