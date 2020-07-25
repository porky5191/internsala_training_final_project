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
import porky.training.zypko.global.CheckConnection
import porky.training.zypko.global.DialogBuilder
import porky.training.zypko.global.JSONParser
import porky.training.zypko.global.NetRequest
import porky.training.zypko.interfaces.ObjectResponse
import porky.training.zypko.interfaces.OnAlertClick
import porky.training.zypko.interfaces.Urls
import porky.training.zypko.interfaces.UserInterface
import porky.training.zypko.pref.UserPreference
import porky.training.zypko.util.RegisterUtil
import java.util.*

class FragRegister : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_register, container, false)
            init()
        }
        return root
    }

    private fun init() {
        /*Initialize the components*/
        tf_name = root!!.findViewById(R.id.tf_name)
        tf_email = root!!.findViewById(R.id.tf_email)
        tf_mobileno = root!!.findViewById(R.id.tf_mobileno)
        tf_delivery = root!!.findViewById(R.id.tf_delivery)
        tf_password = root!!.findViewById(R.id.tf_password)
        tf_confirm = root!!.findViewById(R.id.tf_confirm)
        btn_register = root!!.findViewById(R.id.btn_register)
        progress = root!!.findViewById(R.id.progress)

        userPreference = UserPreference(activity!!)
        util = RegisterUtil()
        request = NetRequest(activity!!)
        jsonParser = JSONParser()

        val registerButton = btn_register
        //handle clickes of registerButton
        registerButton!!.setOnClickListener { v: View? ->
            //check if there is internet or ot
            if (CheckConnection(activity!!).hasConnection()) register()
            else showDialog() }
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

    private fun register() {
        /*get the values from the editTexts*/
        name = tf_name!!.text.toString()
        email = tf_email!!.text.toString()
        mobileNo = tf_mobileno!!.text.toString()
        address = tf_delivery!!.text.toString()
        password = tf_password!!.text.toString()

        //verify whether the editTexts are correctly filled or not
        if (util!!.verify(tf_name!!, tf_email!!, tf_mobileno!!, tf_delivery!!, tf_password!!, tf_confirm!!)) {
            progress!!.visibility = View.VISIBLE
            btn_register!!.isEnabled = false

            val map = HashMap<String?, String?>()
            map[UserInterface.NAME] = name
            map[UserInterface.EMAIL] = email
            map[UserInterface.ADDRESS] = address
            map[UserInterface.PASSWORD] = password
            map[UserInterface.MOBILE_NO] = mobileNo

            //make request
            request!!.post(Urls.REGISTER, map, object : ObjectResponse {
                override fun onSuccess(response: JSONObject?) {

                    progress!!.visibility = View.INVISIBLE
                    btn_register!!.isEnabled = true

                    val `object` = jsonParser!!.getObject(response!!)
                    val user = jsonParser!!.parseUser(`object`)
                    user.password = password
                    if (user.userId == 0) {
                        Toast.makeText(activity!!, "Failed. Try again", Toast.LENGTH_SHORT).show()
                    } else {
                        //save the user details in the SharedPreference
                        userPreference!!.user = user
                        //move to MainActivity after regitered successfully
                        startActivity(Intent(activity!!, MainActivity::class.java))
                        //finish this activity so that user can't navigate back
                        activity!!.finish()
                    }
                }

                override fun onError(error: VolleyError?) {
                    Toast.makeText(activity!!, "Failed. Try again", Toast.LENGTH_SHORT).show()
                    progress!!.visibility = View.INVISIBLE
                    btn_register!!.isEnabled = true
                }
            })
        }
    }

    //-------------------------------------------------------
    private var root: View? = null
    private var userPreference: UserPreference? = null
    private var util: RegisterUtil? = null
    private var request: NetRequest? = null
    private var jsonParser: JSONParser? = null
    private var name: String? = null
    private var email: String? = null
    private var mobileNo: String? = null
    private var address: String? = null
    private var password: String? = null
    private var tf_name: EditText? = null
    private var tf_email: EditText? = null
    private var tf_mobileno: EditText? = null
    private var tf_delivery: EditText? = null
    private var tf_password: EditText? = null
    private var tf_confirm: EditText? = null
    private var btn_register: Button? = null
    private var progress: ProgressBar? = null
}