package porky.training.zypko.fragment.navigation

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import porky.training.zypko.R
import porky.training.zypko.adapter.RestaurantAdapter
import porky.training.zypko.global.AppDatabase
import porky.training.zypko.model.Restaurant
import java.util.*

class FragFavRestaurants : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_fav_restaurant, container, false)
            init()
        }
        return root
    }

    private fun init() {
        //change the title of the toolbar
        activity!!.title = "Favorite Restaurants"
        recycler = root!!.findViewById(R.id.recycler)
        GetLocalData().execute()
        val ll_progress = root!!.findViewById<LinearLayout>(R.id.ll_progress)
        //make the progress layout visible
        //given time for better animated entry
        Handler().postDelayed({ ll_progress.visibility = View.GONE }, 400)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetLocalData internal constructor() : AsyncTask<Void?, Void?, Void?>() {
        private var itemList: ArrayList<Restaurant?>?
        protected override fun doInBackground(vararg voids: Void?): Void? {
            //Initialize instance of localDb
            val database = AppDatabase.getInstance(activity!!)
            //fetch the list from local db
            itemList = database!!.restaurantDao()!!.all as ArrayList<Restaurant?>?
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            //initializing the adapter and setting list to it
            adapter = RestaurantAdapter(activity!!, fragmentManager!!, itemList)
            recycler!!.adapter = adapter
            recycler!!.layoutManager = LinearLayoutManager(activity)
        }

        init {
            itemList = ArrayList()
        }
    }

    //----------------------------------
    private var root: View? = null
    private var recycler: RecyclerView? = null
    private var adapter: RestaurantAdapter? = null
}