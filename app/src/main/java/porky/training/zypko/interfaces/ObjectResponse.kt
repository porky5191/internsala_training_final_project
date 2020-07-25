package porky.training.zypko.interfaces

import com.android.volley.VolleyError
import org.json.JSONObject

interface ObjectResponse {
    fun onSuccess(response: JSONObject?)
    fun onError(error: VolleyError?)
}