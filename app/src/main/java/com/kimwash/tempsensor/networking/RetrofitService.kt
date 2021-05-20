package com.kimwash.tempsensor.networking

import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.*

interface RetrofitService {
    @POST("sensors/record")
    fun recordData(
        @Body sensorData: SensorData
    ): Single<ResponseData>
}