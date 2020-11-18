package com.smilemakers.data.network

import com.smilemakers.utils.ApiExceptions
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.StringBuilder

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (t: Throwable) {
            throw ApiExceptions(t.toString())
        }
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()

            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                    message.append("\n")
                }
            }
            message.append("Error Code: ${response.code()}")
            throw  ApiExceptions(message.toString())
        }
    }
}