package porky.training.zypko.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import porky.training.zypko.R
import porky.training.zypko.model.Menu
import java.util.*

class CartAdapter(private val context: Context, private val arrayList: ArrayList<Menu?>?) : RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_cart, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menu = arrayList?.get(position)
        holder.l_name.text = menu?.name ?: ""
        holder.l_cost.text = "Rs " + (menu?.costOfOne ?: 0)
    }

    override fun getItemCount(): Int {
        return arrayList?.size ?: 0
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var l_name: TextView
        var l_cost: TextView

        init {
            l_name = itemView.findViewById(R.id.l_name)
            l_cost = itemView.findViewById(R.id.l_cost)
        }
    }

}