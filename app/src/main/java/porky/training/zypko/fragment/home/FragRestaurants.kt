package porky.training.zypko.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import org.json.JSONObject
import porky.training.zypko.R
import porky.training.zypko.activity.MainActivity
import porky.training.zypko.adapter.RestaurantAdapter
import porky.training.zypko.global.*
import porky.training.zypko.interfaces.DrawerLocker
import porky.training.zypko.interfaces.ObjectResponse
import porky.training.zypko.interfaces.OnAlertClick
import porky.training.zypko.interfaces.Urls
import porky.training.zypko.model.Restaurant
import porky.training.zypko.util.RestaurantUtil
import kotlin.collections.ArrayList

class FragRestaurants : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_all_restaurant, container, false)
            init()
        }
        return root
    }

    private fun init() {
        recycler = root!!.findViewById(R.id.recycler)
        ll_progress = root!!.findViewById(R.id.ll_progress)

        //initialize different objects
        list = ArrayList()
        favList = ArrayList()
        netRequest = NetRequest(activity!!)
        jsonParser = JSONParser()
        util = RestaurantUtil()

        //initialize the local database
        database = AppDatabase.getInstance(activity!!)

        //enable option menu
        setHasOptionsMenu(true)
        //check if the device has active internet connection
        if (CheckConnection(activity!!).hasConnection()) {
            makeRequest()
        } else {

            ll_progress?.setVisibility(View.GONE)
            //show AlertDialog
            DialogBuilder().init(activity)
                    .setTitle("No Internet")
                    .setMessage("You have no proper internet connection. Click ok to open internet settings")
                    .setPositiveBtn("Ok", object : OnAlertClick {
                        override fun onClick() {
                            //go to networdSettings of device
                            startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                        }
                    })
                    .setNegativeBtn("Cancel", object : OnAlertClick {
                        override fun onClick() {

                        }
                    })
                    .build()
        }
    }

    private fun makeRequest() {
        //loading all restaurants & creating recycler view
        netRequest!![Urls.ALL_RESTAURANTS, object : ObjectResponse {
            override fun onSuccess(response: JSONObject?) {
                //parse the main data part of the response
                val array = jsonParser!!.getArray(response!!)
                //check the length of the parsed array
                if (array.length() == 0) {
                    Toast.makeText(activity!!, "Failed to load data", Toast.LENGTH_SHORT).show()
                    return
                }
                //parse the array & get required data
                list = jsonParser!!.parseRestaurant(array)

                if (list!!.isEmpty()) {
                    Toast.makeText(activity!!, "Failed to load data", Toast.LENGTH_SHORT).show()
                    return
                }
                setAdapter()
            }

            override fun onError(error: VolleyError?) {
                Toast.makeText(activity!!, "Error while loading data!!", Toast.LENGTH_SHORT).show()
            }
        }]
    }

    private fun setAdapter() {
        //initializing the adapter and setting list to it
        adapter = RestaurantAdapter(activity!!, fragmentManager!!, list)
        recycler!!.adapter = adapter
        recycler!!.layoutManager = LinearLayoutManager(activity)
        //get all the favourite restaurants from localDb
        GetLocalData().execute()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //handle item selected
        when (item.itemId) {
            R.id.nav_cost_low_to_high -> {
                val arr1 = util!!.sortCostLowToHigh(list)
                if (arr1.size > 0) updateAdapter(arr1)
            }
            R.id.nav_cost_high_to_low -> {
                val arr2 = util!!.sortCostHighToLow(list)
                if (arr2.size > 0) updateAdapter(arr2)
            }
            R.id.nav_rating -> {
                val arr3 = util!!.sortBtRating(list)
                if (arr3.size > 0) updateAdapter(arr3)
            }
            R.id.nav_name -> {
                val arr4 = util!!.sortByName(list)
                if (arr4.size > 0) updateAdapter(arr4)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    //update the list after sort and notify the adapter
    private fun updateAdapter(arrayList: ArrayList<Restaurant>) {

        ll_progress!!.visibility = View.VISIBLE

        //Initialize a new ArrayList
        val updatedList = ArrayList(arrayList)
        //get the size of the updated list
        val listSize = updatedList.size

        //delete all elements of the list
        list!!.clear()
        //notify adapter that all items have been deleted
        adapter!!.notifyItemRangeRemoved(0, listSize)
        //add updated list to the ArrayList
        list!!.addAll(updatedList)
        //notify adapter that new item has been added
        adapter!!.notifyItemRangeInserted(0, listSize)
        Handler().postDelayed({ ll_progress!!.visibility = View.GONE }, 500)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetLocalData : AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            //get list of all favourite restaurant from localDb
            favList = database!!.restaurantDao()!!.all as ArrayList<Restaurant?>?
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            if (list!!.size != 0) {
                //merge both list and favourite restaurant list
                for (restaurant in favList!!) {
                    for (i in list!!.indices) {
                        val res1 = list!![i]
                        if (restaurant!!.id == res1!!.id) {
                            list!!.removeAt(i)
                            list!!.add(i, restaurant)
                            adapter!!.notifyItemChanged(i)
                            break
                        }
                    }
                }
            }
            ll_progress!!.visibility = View.GONE
        }
    }

    override fun onResume() {
        //change the title of the toolbar
        activity!!.title = "Zypko"

        //show the navigation drawer
        (activity as DrawerLocker?)!!.setDrawerEnable(true)

        if (fragmentManager!!.backStackEntryCount == 0) {
            //if there is fragment in the stack means home fragment is open
            //select the home fragement
            MainActivity.navigationView!!.setCheckedItem(R.id.nav_home)
        }
        super.onResume()
    }

    //-----------defining variables-----------------------------------
    private var root: View? = null
    private var netRequest: NetRequest? = null
    private var list: ArrayList<Restaurant?>? = null
    private var favList: ArrayList<Restaurant?>? = null
    private var adapter: RestaurantAdapter? = null
    private var jsonParser: JSONParser? = null
    private var database: AppDatabase? = null
    private var util: RestaurantUtil? = null
    private var recycler: RecyclerView? = null
    private var ll_progress: LinearLayout? = null
}