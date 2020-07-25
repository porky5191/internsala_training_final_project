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
import porky.training.zypko.global.*
import porky.training.zypko.interfaces.ObjectResponse
import porky.training.zypko.interfaces.OnAlertClick
import porky.training.zypko.interfaces.Urls
import porky.training.zypko.interfaces.UserInterface
import java.util.*

class FragForgotPassword : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_forgot_password, container, false)
            init()
        }
        return root
    }

    //Initialize different variables
    private fun init() {
        tf_mobileno = root!!.findViewById(R.id.tf_mobileno)
        tf_email = root!!.findViewById(R.id.tf_email)
        btn_next = root!!.findViewById(R.id.btn_next)
        progress = root!!.findViewById(R.id.progress)

        opener = FragmentOpener()
        parser = JSONParser()
        netRequest = NetRequest(activity!!)

        val buttonNext = btn_next
        buttonNext?.setOnClickListener { v: View? ->
            //check if device has active internet connection
            if (CheckConnection(activity!!).hasConnection()) {
                //get the string from EditTexts
                val mailAdd = tf_email
                email = mailAdd?.getText().toString()
                val phoneNo = tf_mobileno
                mobile = phoneNo?.getText().toString()
                if (canProceed()) makeRequest()
            } else showDialog()
        }
    }

    private fun showDialog() {
        //show if there is no internet connection
        DialogBuilder().init(activity!!)
                .setTitle("No Internet")
                .setMessage("You have no proper internet connection. Click ok to open internet settings")
                .setPositiveBtn("Ok", object : OnAlertClick {
                    override fun onClick() {
                        //open Internet Settings page
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }
                })
                .setNegativeBtn("Cancel", object : OnAlertClick {
                    override fun onClick() {

                    }
                })
                .build()
    }

    //check if inputs are correcte
    private fun canProceed(): Boolean {
        val canProceed = true
        if (email!!.isEmpty() || mobile!!.isEmpty()) {
            Toast.makeText(activity!!, "fields can't be empty", Toast.LENGTH_SHORT).show()
            return false
        } else if (mobile!!.length != 10) {
            Toast.makeText(activity!!, "invalid mobile no", Toast.LENGTH_SHORT).show()
            return false
        }
        return canProceed
    }

    //Make request to server to send Mobile no & email
    private fun makeRequest() {
        btn_next!!.isEnabled = false
        progress!!.visibility = View.VISIBLE

        //put the parameters into a hashmap
        val map = HashMap<String?, String?>()
        map[UserInterface.MOBILE_NO] = mobile
        map[UserInterface.EMAIL] = email

        //make the request to the server
        netRequest!!.post(Urls.FORGOT_PASSWORD, map, object : ObjectResponse {
            //whenever request is success
            override fun onSuccess(response: JSONObject?) {
                progress!!.visibility = View.INVISIBLE
                btn_next!!.isEnabled = true

                //get value of success from the response
                if (parser!!.getSuccess(response!!)) {
                    //open Varify Otp fragment
                    opener!!.open(FragVerifyOtp(mobile), fragmentManager!!, true, true, false)
                } else Toast.makeText(activity!!, "Failed. Try again", Toast.LENGTH_SHORT).show()
            }
            //whenever some error occurs
            override fun onError(error: VolleyError?) {
                Toast.makeText(activity!!, "Failed. Try again", Toast.LENGTH_SHORT).show()
                progress!!.visibility = View.INVISIBLE
                btn_next!!.isEnabled = true
            }
        })
    }

    //--------------------------------------------
    private var root: View? = null
    private var opener: FragmentOpener? = null
    private var netRequest: NetRequest? = null
    private var parser: JSONParser? = null
    private var email: String? = null
    private var mobile: String? = null
    private var tf_mobileno: EditText? = null
    private var tf_email: EditText? = null
    private var btn_next: Button? = null
    private var progress: ProgressBar? = null
}