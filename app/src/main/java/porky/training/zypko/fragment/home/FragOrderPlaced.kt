package porky.training.zypko.fragment.home

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import porky.training.zypko.R
import porky.training.zypko.global.AppDatabase

class FragOrderPlaced : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_order_placed, container, false)
            init()
        }
        return root
    }

    private fun init() {
        //change the title of the toolbar
        activity!!.title = "Order Placed"

        //Initialize the lottie animation and define diffent params
        animation = root!!.findViewById(R.id.progress)
        val localAnimation = animation
        if (localAnimation != null) {
            localAnimation.setSpeed(2.0f)
            localAnimation.setProgress(50f)
        }

        //initialize button ok
        btn_ok = root!!.findViewById(R.id.btn_ok)

        //delete all item from cart using Asynctask
        GetAsync().execute()

        //define a local var for button ok
        val okButton = btn_ok
        //handle click event
        okButton!!.setOnClickListener { v: View? ->
            val fm = fragmentManager!!
            //remove all the fragment added to Stack leaving only the FragRestaurant
            for (i in 0 until fm.backStackEntryCount) {
                fm.popBackStack()
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class GetAsync : AsyncTask<Void?, Void?, Void?>() {
        var database: AppDatabase? = null
        override fun doInBackground(vararg voids: Void?): Void? {
            //initialize the database
            database = AppDatabase.getInstance(activity!!)
            //delete all food items previously added to the cart from the localDatabase
            val localDb = database
            localDb!!.menuDao()!!.deleteAll()
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            //make the button visible to navigate to the home fragment
            btn_ok!!.visibility = View.VISIBLE
        }
    }

    //------------------------------------
    private var root: View? = null
    private var animation: LottieAnimationView? = null
    private var btn_ok: Button? = null
}