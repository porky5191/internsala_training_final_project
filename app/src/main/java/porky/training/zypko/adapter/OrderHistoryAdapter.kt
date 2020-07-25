package porky.training.zypko.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import porky.training.zypko.R
import porky.training.zypko.model.OrderHistory
import java.util.*

class OrderHistoryAdapter(private val context: Context, private val arrayList: ArrayList<OrderHistory?>?) : RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_order_history, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val orderHistory = arrayList?.get(position)
        holder.l_hotelName.text = orderHistory?.restaurantName ?: ""

        //get the date time string
        var orderDate = orderHistory?.orderPlacedAt
        //05-07-20 15:25:15
        //split the string and get only the date part
        orderDate = orderDate?.substring(0, 9)
        //set date to the textview
        holder.l_dateOfOrder.text = orderDate

        //call the child adapter to show all the food items ordered from the hotels
        val childAdapter = orderHistory?.foodList?.let { ChildAdapter(context, it) }
        //set Adapter to the recycler view
        holder.recyclerParent.adapter = childAdapter
        holder.recyclerParent.setHasFixedSize(true)
        //initialize layout manager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //set layout manager to recycler view
        holder.recyclerParent.layoutManager = layoutManager
    }

    override fun getItemCount(): Int {
        return arrayList?.size ?: 0
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var l_hotelName: TextView
        var l_dateOfOrder: TextView
        var recyclerParent: RecyclerView

        init {
            l_hotelName = itemView.findViewById(R.id.l_hotelName)
            l_dateOfOrder = itemView.findViewById(R.id.l_dateOfOrder)
            recyclerParent = itemView.findViewById(R.id.recyclerParent)
        }
    }

}