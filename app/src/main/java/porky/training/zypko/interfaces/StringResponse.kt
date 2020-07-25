package porky.training.zypko.interfaces

import com.android.volley.VolleyError

interface StringResponse {
    fun onSuccess(response: String?)
    fun onError(error: VolleyError?)
}