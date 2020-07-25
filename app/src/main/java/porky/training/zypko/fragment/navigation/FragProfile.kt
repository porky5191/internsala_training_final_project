package porky.training.zypko.fragment.navigation

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import porky.training.zypko.R
import porky.training.zypko.pref.UserPreference

class FragProfile : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_profile, container, false)
            init()
        }
        return root
    }

    private fun init() {
        //change the title of the toolbar
        activity!!.title = "My Profile"
        val l_name = root!!.findViewById<TextView>(R.id.l_name)
        val l_mobileNo = root!!.findViewById<TextView>(R.id.l_mobileNo)
        val l_email = root!!.findViewById<TextView>(R.id.l_email)
        val l_address = root!!.findViewById<TextView>(R.id.l_address)
        val ll_progress = root!!.findViewById<LinearLayout>(R.id.ll_progress)

        //get all details of user from SharedPreference
        val user = UserPreference(activity).user

        //set user details to textVIews
        l_name.text = user.name
        l_mobileNo.text = user.mobileNo
        l_email.text = user.email
        l_address.text = user.address

        //make the progress layout visible
        //given time for better animated entry
        Handler().postDelayed({ ll_progress.visibility = View.GONE }, 400)
    }

    //---------------------------------------------------
    private var root: View? = null
}