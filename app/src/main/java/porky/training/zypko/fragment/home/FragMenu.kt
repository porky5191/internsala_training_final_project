package porky.training.zypko.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import org.json.JSONObject
import porky.training.zypko.R
import porky.training.zypko.adapter.MenuAdapter
import porky.training.zypko.global.*
import porky.training.zypko.interfaces.*
import porky.training.zypko.model.Menu
import porky.training.zypko.model.Restaurant
import java.util.*

class FragMenu(private val restaurant: Restaurant) : Fragment(), AddToCartInterface {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_menu, container, false)
            init()
        }
        return root
    }

    private fun init() {
        //change the title of the toolbar
        activity!!.title = restaurant.name
        //hide the drawer from the tool bar
        (activity as DrawerLocker?)!!.setDrawerEnable(false)

        //enable home button here
        setHasOptionsMenu(true)

        recycler = root!!.findViewById(R.id.recycler)
        l_proceedToCart = root!!.findViewById(R.id.l_proceedToCart)
        ll_progress = root!!.findViewById(R.id.ll_progress)

        //initialize different objects
        netRequest = NetRequest(activity!!)
        jsonParser = JSONParser()
        opener = FragmentOpener()
        list = ArrayList()
        orderedList = ArrayList()
        cartList = ArrayList()
        checkConnection = CheckConnection(activity!!)

        //check if there is internet connection or not
        if (checkConnection!!.hasConnection()) {
            //make request to server
            makeRequest()
        } else {
            //if no internet connection is there
            val progress = ll_progress
            if (progress != null)
                progress.setVisibility(View.GONE)
            //show AlertDialog to navigate to InternetSettings
            showDialog()
        }


        //declare a local var for ProceedToCart button
        val procedToCart = l_proceedToCart
        //handle click event of proceed to cart button
        if (procedToCart != null)
            procedToCart.setOnClickListener(View.OnClickListener { v: View? -> GetAsync(true, false).execute() })
    }

    private fun showDialog() {
        //show this dialog if there is no internet
        DialogBuilder().init(activity)
                .setTitle("No Internet")
                .setMessage("You have no proper internet connection. Click ok to open internet settings")
                .setPositiveBtn("Ok", object : OnAlertClick {
                    override fun onClick() {
                        //handles clicked if 'ok' button is clicked
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }
                })
                .setNegativeBtn("Cancel", object : OnAlertClick {
                    override fun onClick() {
                        //handle click if 'cancel' is clicked
                    }
                })
                .build()
    }

    private fun makeRequest() {
        //make request to server to load all the menu items of a hotem
        netRequest!![Urls.MENU + restaurant.id, object : ObjectResponse {
            override fun onSuccess(response: JSONObject?) {
                //hide progress bar showing layout
                ll_progress!!.visibility = View.GONE

                //parse json array from JsonParser class
                val array = jsonParser!!.getArray(response!!)
                //check if parsed array length is 0
                if (array.length() == 0) {
                    Toast.makeText(activity!!, "Failed to load data", Toast.LENGTH_SHORT).show()
                    return
                }
                //parse ArrayList of menu using JsonParser class
                list = jsonParser!!.parseMenu(array)
                if (list!!.isEmpty()) {
                    Toast.makeText(activity!!, "Failed to load data", Toast.LENGTH_SHORT).show()
                    return
                }
                //call fn to set adpter
                setAdapter()
            }

            override fun onError(error: VolleyError?) {
                //if some volly error occured then it is handled by this part
                ll_progress!!.visibility = View.GONE
                Toast.makeText(activity!!, "Error while loading data!!", Toast.LENGTH_SHORT).show()
            }
        }]
    }

    private fun setAdapter() {
        //initializing the adapter and setting list to it
        adapter = MenuAdapter(activity!!, list, this@FragMenu)
        recycler!!.adapter = adapter
        recycler!!.layoutManager = LinearLayoutManager(activity)
        //get all the items present in the cart, from localDb
        GetAsync(false, true).execute()
    }

    private fun mergeList() {
        //check if there is any item already added to the cart
        if (cartList!!.size != 0) {
            //check if the item already added are from same restaurant
            if (cartList!![0]!!.restaurant_id != restaurant.id) return
            l_proceedToCart!!.visibility = View.VISIBLE
            //marge cartList we fetched from localDb with the list fetched & notify the adapter
            for (menu in cartList!!) {
                for (i in list!!.indices) {
                    val menu1 = list!![i]
                    if (menu!!.id == menu1!!.id) {
                        //remove the item from the list
                        list!!.removeAt(i)
                        //add updated item to the list at the same position
                        list!!.add(i, menu)
                        //notify adapter that few fields have been changed
                        //changes reflects in the recyclerView after notified
                        adapter!!.notifyItemChanged(i)
                        break
                    }
                }
            }
        }
    }

    override fun update(list: ArrayList<Menu?>?) {
        //whenever an item is added or removed gets reflected here
        //set the visibility of the "proceed to cart" button
        if (list!!.size > 0) l_proceedToCart!!.visibility = View.VISIBLE else l_proceedToCart!!.visibility = View.GONE
        //replace the old ordered list with the updated list
        orderedList = list
    }

    //This class will add all the selected item list to the LocalDb
    @SuppressLint("StaticFieldLeak")
    internal inner class GetAsync(addToDb: Boolean, getFromDb: Boolean) : AsyncTask<Void?, Void?, Void?>() {
        var database: AppDatabase?
        var addToDb = false
        var getFromDb = false

        override fun doInBackground(vararg params: Void?): Void? {
            if (addToDb) {
                //delete all elements before adding the new list of items
                database!!.menuDao()!!.deleteAll()
                for (menu in orderedList!!) {
                    //add all the items one by one
                    database!!.menuDao()!!.insert(menu)
                }
            } else if (getFromDb) {
                //get all items added in the cart
                cartList = database!!.menuDao()!!.all as ArrayList<Menu?>?
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            //if an item is add/remove from menu list
            //make changes in LocalDb
            if (addToDb) {
                //after adding all items items to the localDb move to Fragment Cart
                opener!!.open(FragCart(restaurant, orderedList), fragmentManager!!,
                        false, true, false)
            } else if (getFromDb) {
                mergeList()
                //getRestaurant id of whose items are already added to cart
                val cartId = if (cartList!!.size > 0) cartList!![0]!!.restaurant_id else 0
                //calling the adapter
                adapter!!.setCartRestaurantId(cartId)
            }
            super.onPostExecute(aVoid)
        }

        init {
            this.addToDb = addToDb
            this.getFromDb = getFromDb
            //Initialize the database
            database = AppDatabase.getInstance(activity!!)!!
        }

    }

    override fun onCreateOptionsMenu(menu: android.view.Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val fm = fragmentManager!!
            fm.popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        //set the title to the resaturant name as soon as the FragMenu resumes
        activity!!.title = restaurant.name
    }

    //------------Define var--------------------
    private var root: View? = null
    private var netRequest: NetRequest? = null
    private var jsonParser: JSONParser? = null
    private var list: ArrayList<Menu?>? = null
    private var orderedList: ArrayList<Menu?>? = null
    private var cartList: ArrayList<Menu?>? = null
    private var adapter: MenuAdapter? = null
    private var opener: FragmentOpener? = null
    private var checkConnection: CheckConnection? = null
    private var recycler: RecyclerView? = null
    private var l_proceedToCart: TextView? = null
    private var ll_progress: LinearLayout? = null

}