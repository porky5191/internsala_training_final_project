package porky.training.zypko.fragment.login

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.VolleyError
import org.json.JSONObject
import porky.training.zypko.R
import porky.training.zypko.activity.MainActivity
import porky.training.zypko.global.*
import porky.training.zypko.interfaces.ObjectResponse
import porky.training.zypko.interfaces.OnAlertClick
import porky.training.zypko.interfaces.Urls
import porky.training.zypko.interfaces.UserInterface
import java.util.*

class FragVerifyOtp internal constructor(private val mobileNo: String?) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_verify_otp, container, false)
            init()
        }
        return root
    }

    private fun init() {
        tf_otp = root!!.findViewById(R.id.tf_otp)
        tf_password = root!!.findViewById(R.id.tf_password)
        tf_confirm = root!!.findViewById(R.id.tf_confirm)
        progress = root!!.findViewById(R.id.progress)
        btn_login = root!!.findViewById(R.id.btn_login)

        opener = FragmentOpener()
        parser = JSONParser()
        netRequest = NetRequest(activity!!)

        val loginButton = btn_login
        //handle button clicks of logIn Button
        loginButton?.setOnClickListener { v: View? ->
            //check if it has internet connection
            if (CheckConnection(activity!!).hasConnection()) {
                val localOtp = tf_otp
                otp = localOtp?.getText().toString()
                val localPassword = tf_password
                password = localPassword?.getText().toString()
                val localConfirm= tf_confirm
                confirm = localConfirm?.getText().toString()
                if (canProceed()) makeRequest()
            } else showDialog()
        }
    }

    private fun showDialog() {
        DialogBuilder().init(activity)
                .setTitle("No Internet")
                .setMessage("You have no proper internet connection. Click ok to open internet settings")
                .setPositiveBtn("Ok", object : OnAlertClick {
                    override fun onClick() {
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }
                })
                .setNegativeBtn("Cancel", object : OnAlertClick {
                    override fun onClick() {

                    }
                })
                .build()
    }

    private fun canProceed(): Boolean {
        val canProceed = true
        if (otp!!.isEmpty() || password!!.isEmpty() || confirm!!.isEmpty()) {
            Toast.makeText(activity!!, "Fields can't be empty", Toast.LENGTH_SHORT).show()
            return false
        } else if (password != confirm) {
            Toast.makeText(activity!!, "Both password doesn't match", Toast.LENGTH_SHORT).show()
            return false
        } else if (password!!.length < 6) {
            Toast.makeText(activity!!, "Minumum length of password must be 6", Toast.LENGTH_SHORT).show()
            return false
        }
        return canProceed
    }

    private fun makeRequest() {
        progress!!.visibility = View.VISIBLE
        btn_login!!.isEnabled = false

        val map = HashMap<String?, String?>()
        map[UserInterface.MOBILE_NO] = mobileNo
        map[UserInterface.PASSWORD] = password
        map[UserInterface.OTP] = otp

        //make requst to server
        netRequest!!.post(Urls.RESET_PASSWORD, map, object : ObjectResponse {
            override fun onSuccess(response: JSONObject?) {

                progress!!.visibility = View.INVISIBLE
                btn_login!!.isEnabled = true

                //check the value of success from the response
                if (parser!!.getSuccess(response!!)) {
                    //if password is changed successfully then move to MainActivity
                    startActivity(Intent(activity!!, MainActivity::class.java))
                    //finish the current activity
                    activity!!.finish()
                } else Toast.makeText(activity!!, "Failed. Try again", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: VolleyError?) {
                progress!!.visibility = View.INVISIBLE
                btn_login!!.isEnabled = true
            }
        })
    }

    //-----------------------------
    private var root: View? = null
    private var opener: FragmentOpener? = null
    private var netRequest: NetRequest? = null
    private var parser: JSONParser? = null
    private var otp: String? = null
    private var password: String? = null
    private var confirm: String? = null
    private var tf_otp: EditText? = null
    private var tf_password: EditText? = null
    private var tf_confirm: EditText? = null
    private var progress: ProgressBar? = null
    private var btn_login: Button? = null

}