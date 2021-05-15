package com.kimwash.tempsensor.networking

import android.os.AsyncTask
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class Post(private val URL:String, private val json:JSONObject) : AsyncTask<Void, JSONObject, JSONObject>() {
    override fun doInBackground(vararg params: Void?): JSONObject {
        val JSON: MediaType = "application/json; charset=utf-8".toMediaTypeOrNull()!!
        val reqBody = json.toString().toRequestBody(JSON)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL)
            .post(reqBody)
            .build()
        val response = client.newCall(request).execute().body!!.string()
        return JSONObject(response)
    }

}