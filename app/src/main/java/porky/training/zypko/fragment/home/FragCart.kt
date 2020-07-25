package porky.training.zypko.fragment.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import porky.training.zypko.R
import porky.training.zypko.adapter.CartAdapter
import porky.training.zypko.global.CheckConnection
import porky.training.zypko.global.DialogBuilder
import porky.training.zypko.global.FragmentOpener
import porky.training.zypko.global.NetRequest
import porky.training.zypko.interfaces.*
import porky.training.zypko.model.Menu
import porky.training.zypko.model.Restaurant
import porky.training.zypko.model.User
import porky.training.zypko.pref.UserPreference
import java.util.*

class FragCart internal constructor(private val restaurant: Restaurant, private val cartList: ArrayList<Menu?>?) : Fragment(), PlaceOrderInterface {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_cart, container, false)
            init()
        }
        return root
    }

    private fun init() {
        //change the title of the toolbar
        activity!!.title = "My Cart"

        //initialize all the widget
        recycler = root!!.findViewById(R.id.recycler)
        l_placeOrder = root!!.findViewById(R.id.l_placeOrder)
        l_hotelName = root!!.findViewById(R.id.l_hotelName)
        progress = root!!.findViewById(R.id.progress)

        //set hotel name
        val hotelName = l_hotelName
        hotelName!!.setText(restaurant.name)

        //initialize diffetent Instance
        opener = FragmentOpener()
        netRequest = NetRequest(activity!!)
        user = UserPreference(activity!!).user

        //initialize local var of button and progressbar
        val placeOrder = l_placeOrder
        val progressBar = progress
        //handle click event at place order button
        placeOrder!!.setOnClickListener { v: View? ->
            //check if it has internet connection
            if (CheckConnection(activity!!).hasConnection()) {
                //make progressBar visible
                progressBar!!.setVisibility(View.VISIBLE)
                //disable the button so that it won't work once clicked
                // until previous work is finished
                placeOrder.setEnabled(false)
                //call fn to place order
                placeOrder()
            } else {
                //if no internet present then show AlertDialog
                showDialog()
            }
        }

        //set adapter to show the list of items in cart
        setAdapter()
    }

    private fun showDialog() {
        DialogBuilder().init(activity!!)
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

    private fun setAdapter() {
        //initialize adapter
        adapter = CartAdapter(activity!!, cartList)
        //set adapter to the recycler
        recycler!!.adapter = adapter
        recycler!!.layoutManager = LinearLayoutManager(activity)

        //find the total cost of items
        for (menu in cartList!!) {
            totalCost = totalCost + (menu?.costOfOne ?: 0.00)
        }
        //set text to the place order button
        l_placeOrder!!.text = "Place Order (Total Rs $totalCost)"
    }

    private fun placeOrder() {

        //put all the food id in a JSONArray
        val array = JSONArray()
        for (menu in cartList!!) {
            val `object` = JSONObject()
            try {
                //put the food id in a new JSONObject
                `object`.put(PlaceOrderInterface.FOOD_ITEM_ID, menu?.id)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            //put the JSONObject in the JSONArray created
            array.put(`object`)
        }

        //put all the details in a json array
        val jsonObject = JSONObject()
        try {
            //put user id in the jsonObject
            jsonObject.put(PlaceOrderInterface.USER_ID, user!!.userId)
            //put restaurant id in the jsonObject
            jsonObject.put(PlaceOrderInterface.RESTAURANT_ID, cartList?.get(0)?.restaurant_id)
            //put total cost in the jsonObject
            jsonObject.put(PlaceOrderInterface.TOTAL_COST, totalCost)
            //put jsonArray of all food items ordered in the object
            jsonObject.put(PlaceOrderInterface.FOOD, array)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        //make the request to the server
        netRequest!!.post(Urls.PLACE_ORDER, jsonObject, object : ObjectResponse {
            override fun onSuccess(response: JSONObject?) {
                //make progress bar leave the screen
                progress!!.visibility = View.GONE
                //placeOrder button is enabled
                // can be clicked again
                l_placeOrder!!.isEnabled = true

                try {
                    //parse the response and get data from the json
                    val `object` = response!!.getJSONObject(GlobalInterface.DATA)
                    //get success from the json object
                    val success = `object`.getBoolean(GlobalInterface.SUCCESS)
                    if (success) {
                        //if success is true
                        opener!!.open(FragOrderPlaced(), fragmentManager!!, false, true, true)
                    } else {
                        //if success is false
                        Toast.makeText(activity!!, "Failed to place order. Try again", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    //if some problem occured while parsing json
                    progress!!.visibility = View.GONE
                    l_placeOrder!!.isEnabled = true
                }
            }

            override fun onError(error: VolleyError?) {
                //if some error occured while making the request
                Toast.makeText(activity!!, "Failed to place order. Try again", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        activity!!.title = "My Cart"
    }

    //-------------------------------------
    private var root: View? = null
    private var opener: FragmentOpener? = null
    private var adapter: CartAdapter? = null
    private var totalCost = 0.0
    private var netRequest: NetRequest? = null
    private var user: User? = null
    private var l_placeOrder: TextView? = null
    private var l_hotelName: TextView? = null
    private var recycler: RecyclerView? = null
    private var progress: ProgressBar? = null

}