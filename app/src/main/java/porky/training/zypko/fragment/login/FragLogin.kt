package porky.training.zypko.fragment.login

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import porky.training.zypko.pref.UserPreference
import java.util.*

class FragLogin : Fragment(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_login, container, false)
            init()
        }
        return root
    }

    private fun init() {
        l_signup = root!!.findViewById(R.id.l_signup)
        l_forgot_pass = root!!.findViewById(R.id.l_forgot_pass)
        tf_mobileno = root!!.findViewById(R.id.tf_mobileno)
        tf_password = root!!.findViewById(R.id.tf_password)
        btn_login = root!!.findViewById(R.id.btn_login)
        progress = root!!.findViewById(R.id.progress)

        val signUp = l_signup
        signUp?.setOnClickListener(this)
        val forgotPass = l_forgot_pass
        forgotPass?.setOnClickListener(this)
        val login = btn_login
        login?.setOnClickListener(this)

        opener = FragmentOpener()
        parser = JSONParser()
        request = NetRequest(activity!!)
        preference = UserPreference(activity)
    }

    override fun onClick(v: View) {
        val id = v.id
        when (id) {
            R.id.l_signup -> {
                assert(fragmentManager != null)
                opener!!.open(FragRegister(), fragmentManager!!, true, true, false)
            }
            R.id.l_forgot_pass -> {
                assert(fragmentManager != null)
                opener!!.open(FragForgotPassword(), fragmentManager!!, true, true, false)
            }
            R.id.btn_login -> if (CheckConnection(activity!!).hasConnection()) {
                if (canProceed()) {
                    btn_login!!.isEnabled = false
                    progress!!.visibility = View.VISIBLE
                    login()
                }
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
        if (tf_mobileno!!.text.toString().isEmpty() || tf_password!!.text.toString().isEmpty()) {
            Toast.makeText(activity!!, "fields can't be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (tf_mobileno!!.text.toString().length != 10) {
            Toast.makeText(activity!!, "mobile number must have 10 digit", Toast.LENGTH_SHORT).show()
            return false
        }
        return canProceed
    }

    private fun login() {
        //put parameters into HashMap
        val map = HashMap<String?, String?>()
        map[UserInterface.MOBILE_NO] = tf_mobileno!!.text.toString()
        map[UserInterface.PASSWORD] = tf_password!!.text.toString()

        //make request to server
        request!!.post(Urls.LOGIN, map, object : ObjectResponse {
            override fun onSuccess(response: JSONObject?) {

                progress!!.visibility = View.INVISIBLE
                btn_login!!.isEnabled = true
                //get the data in the form of JSONObject
                val `object` = parser!!.getObject(response!!)
                //parse the JSONObject and put it into model user
                val user = parser!!.parseUser(`object`)
                if (user.userId == 0) {
                    Toast.makeText(activity!!, "Failed. Try again", Toast.LENGTH_LONG).show()
                } else {
                    preference!!.user = user
                    //if login succeeded
                    //move to MainActivity
                    startActivity(Intent(activity!!, MainActivity::class.java))
                    //finish the StartActivity so that user can't navigate back
                    activity!!.finish()
                }
            }

            override fun onError(error: VolleyError?) {
                progress!!.visibility = View.INVISIBLE
                btn_login!!.isEnabled = true
                Toast.makeText(activity!!, "Failed. Try again", Toast.LENGTH_LONG).show()
            }
        })
    }

    //-------------------------------------------------------
    private var root: View? = null
    private var opener: FragmentOpener? = null
    private var preference: UserPreference? = null
    private var request: NetRequest? = null
    private var parser: JSONParser? = null
    private var l_signup: TextView? = null
    private var l_forgot_pass: TextView? = null
    private var tf_mobileno: EditText? = null
    private var tf_password: EditText? = null
    private var btn_login: Button? = null
    private var progress: ProgressBar? = null
}