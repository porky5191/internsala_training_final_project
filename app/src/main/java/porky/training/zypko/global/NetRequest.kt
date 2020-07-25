package porky.training.zypko.global

import android.content.Context
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import porky.training.zypko.interfaces.ObjectResponse
import porky.training.zypko.interfaces.Urls
import java.util.*

class NetRequest(private val context: Context) {
    //for making GET request
    operator fun get(url: String?, objectResponse: ObjectResponse) {
        //initialize request queue
        val queue = Volley.newRequestQueue(context)
        //JsonObject request
        val getRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, Response.Listener { response: JSONObject? -> objectResponse.onSuccess(response) }, Response.ErrorListener { error: VolleyError? -> objectResponse.onError(error) }) {
            override fun getHeaders(): Map<String, String> {
                //adding header to the request
                val map = HashMap<String, String>()
                map["Content-type"] = "application/json"
                map["token"] = Urls.MY_TOKEN
                return map
            }
        }
        //add request to the queue
        queue.add(getRequest)
    }

    /*
    Content-Type : application/json
    token :
     */
    //for making POST request
    fun post(url: String?, map: HashMap<String?, String?>?, objectResponse: ObjectResponse) {
        //Initialize request queue
        val queue = Volley.newRequestQueue(context)
        //making JsonObject request
        val jsonObjReq: JsonObjectRequest = object : JsonObjectRequest(Method.POST,
                url, JSONObject(map as Map<*, *>), Response.Listener { response: JSONObject? -> objectResponse.onSuccess(response) }, Response.ErrorListener { error: VolleyError? -> objectResponse.onError(error) }) {
            override fun getHeaders(): Map<String, String> {
                //adding header to the request
                val map = HashMap<String, String>()
                map["Content-type"] = "application/json"
                map["token"] = Urls.MY_TOKEN
                return map
            }
        }

        // Adding request to request queue
        queue.add(jsonObjReq)
    }

    //making POST request
    //instead of passing HashMap we are directly passing JSONObject here
    fun post(url: String?, `object`: JSONObject?, objectResponse: ObjectResponse) {
        val queue = Volley.newRequestQueue(context)
        val jsonObjReq: JsonObjectRequest = object : JsonObjectRequest(Method.POST,
                url, `object`, Response.Listener { response: JSONObject? -> objectResponse.onSuccess(response) }, Response.ErrorListener { error: VolleyError? -> objectResponse.onError(error) }) {
            override fun getHeaders(): Map<String, String> {
                //adding header to the request
                val map = HashMap<String, String>()
                map["Content-type"] = "application/json"
                map["token"] = Urls.MY_TOKEN
                return map
            }
        }
        // Adding request to request queue
        queue.add(jsonObjReq)
    }

}