package porky.training.zypko.fragment.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import org.json.JSONObject
import porky.training.zypko.R
import porky.training.zypko.adapter.OrderHistoryAdapter
import porky.training.zypko.global.JSONParser
import porky.training.zypko.global.NetRequest
import porky.training.zypko.interfaces.ObjectResponse
import porky.training.zypko.interfaces.Urls
import porky.training.zypko.model.OrderHistory
import porky.training.zypko.model.User
import porky.training.zypko.pref.UserPreference
import java.util.*

class FragOrderHistory : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_order_history, container, false)
            init()
        }
        return root
    }

    private fun init() {
        //set the title of the toolbar
        activity!!.title = "My Previous Orders"
        //get user from the sharedPreference
        user = UserPreference(activity!!).user

        recycler = root!!.findViewById(R.id.recycler)
        ll_progress = root!!.findViewById(R.id.ll_progress)
        itemList = ArrayList()
        parser = JSONParser()

        //send request to server to load Previously oredered items
        makeRequest()
    }

    //3124 "3122"
    private fun makeRequest() {
        //load all the previous orders from server
        assert(activity != null)
        NetRequest(activity!!)[Urls.ORDER_HISTORY + user!!.userId, object : ObjectResponse {
            override fun onSuccess(response: JSONObject?) {
                //make progress layout bar gone
                ll_progress!!.visibility = View.GONE

                //check if the success value in the response is true
                if (parser!!.getSuccess(response!!)) {
                    //parse the data from the response and return a JSONArray
                    val array = parser!!.getArray(response)
                    if (array.length() > 0) {
                        //parse the json array and and return a ArrayList<OrderHistory>
                        itemList = parser!!.parseOrderHistory(array)
                        //call fn to setAdapter
                        setAdapter()
                    } else {
                        //if array length is 0
                        Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    //if success value is false
                    Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(error: VolleyError?) {
                //if request fails and error occured
                ll_progress!!.visibility = View.GONE
                Toast.makeText(activity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        }]
    }

    private fun setAdapter() {
        //Initialize the adapter
        val adapter = OrderHistoryAdapter(activity!!, itemList!!)
        //set adapter to the recyclerView
        recycler!!.adapter = adapter
        recycler!!.setHasFixedSize(true)
        //Initialize LinearLayout manager
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        //set layoutManager to the recyclerView
        recycler!!.layoutManager = layoutManager
        //adding divider to the recycler view
        recycler!!.addItemDecoration(DividerItemDecoration(recycler!!.context, DividerItemDecoration.VERTICAL))
    }

    //--------------Define Variables--------------
    private var root: View? = null
    private var user: User? = null
    private var parser: JSONParser? = null
    private var itemList: ArrayList<OrderHistory?>? = null
    private var recycler: RecyclerView? = null
    private var ll_progress: LinearLayout? = null
}