package porky.training.zypko.fragment.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import porky.training.zypko.R
import porky.training.zypko.activity.MainActivity
import porky.training.zypko.global.FragmentOpener
import porky.training.zypko.model.User
import porky.training.zypko.pref.UserPreference

class FragSplash : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_splash, container, false)
            init()
        }
        return root
    }

    private fun init() {
        opener = FragmentOpener()

        //wait for 1sec the execute the code inside
        Handler().postDelayed({
            try {
                //get user from the sharedPreference
                user = UserPreference(activity!!).user
                //check if userId is greater than 0
                //a valid userId is always a positive integer greater than 0
                if (user!!.userId > 0) {
                    //if already loged in then move to MainActivity
                    startActivity(Intent(activity!!, MainActivity::class.java))
                    //finish current activity
                    activity!!.finish()
                } else throw Exception("unable to read")
            } catch (e: Exception) {
                //if not loged in then open logIn screen
                opener!!.open(FragLogin(), fragmentManager!!, true, false, false)
            }
        }, 1000)
    }

    //-----------------------------------------------------------------
    private var root: View? = null
    private var user: User? = null
    private var opener: FragmentOpener? = null
}