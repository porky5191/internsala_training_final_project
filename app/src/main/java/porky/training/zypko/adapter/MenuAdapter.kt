package porky.training.zypko.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import porky.training.zypko.R
import porky.training.zypko.global.AppDatabase
import porky.training.zypko.global.DialogBuilder
import porky.training.zypko.interfaces.AddToCartInterface
import porky.training.zypko.interfaces.OnAlertClick
import porky.training.zypko.model.Menu
import java.util.*

class MenuAdapter(private val context: Context, private val arrayList: ArrayList<Menu?>?, private val addToCart: AddToCartInterface) : RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {
    fun setCartRestaurantId(restaurantId: Int) {
        cartRestaurantId = restaurantId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_menu, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var position = position
        val menu = arrayList?.get(position)
        holder.l_name.text = menu?.name ?: ""
        holder.l_counter.setText((++position).toString())
        holder.l_cost.text = "Rs " + (menu?.costOfOne ?: 0.00)

        //set icons based on whether the food items is added to cart or not
        if (menu!!.isOrdered) {
            ordedItems.add(menu)
            addToCart.update(ordedItems)
            setVisibility(holder, true)
        } else setVisibility(holder, false)

        //click events of buttons if item to add to cart
        holder.iv_add.setOnClickListener { v: View? ->

            //check if cart has items from current restaurant or not
            if (cartRestaurantId != menu.restaurant_id && cartRestaurantId > 0) {
                //show dialog box if cart has FoodItems from different restaurant to clear cart
                DialogBuilder()
                        .init(context)
                        .setTitle("Clear Cart")
                        .setMessage("You can order from one restaurant only. Click OK to clear previously added items")
                        .setPositiveBtn("Ok", object : OnAlertClick {
                            override fun onClick() {
                                //call async task to clear cart
                                GetAsync(menu, holder).execute()
                            }
                        })
                        .setNegativeBtn("Cancel", object : OnAlertClick {
                            override fun onClick() {

                            }
                        })
                        .setCelable(false).build()
            } else {
                menu.isOrdered = true
                setVisibility(holder, true)
                ordedItems.add(menu)
                addToCart.update(ordedItems)
            }
        }

        //handle click event if item has to deleted from cart
        holder.iv_remove.setOnClickListener { v: View? ->
            menu.isOrdered = false
            setVisibility(holder, false)
            ordedItems.remove(menu)
            addToCart.update(ordedItems)
        }
    }

    //set the icons to the image view (either + or - icon)
    private fun setVisibility(holder: MyViewHolder, ordered: Boolean) {
        if (ordered) {
            holder.iv_add.visibility = View.GONE
            holder.iv_remove.visibility = View.VISIBLE
        } else {
            holder.iv_add.visibility = View.VISIBLE
            holder.iv_remove.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return arrayList?.size ?: 0
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var l_counter: TextView
        var l_name: TextView
        var l_cost: TextView
        var iv_add: ImageView
        var iv_remove: ImageView

        init {
            l_counter = itemView.findViewById(R.id.l_counter)
            l_name = itemView.findViewById(R.id.l_name)
            l_cost = itemView.findViewById(R.id.l_cost)
            iv_add = itemView.findViewById(R.id.iv_add)
            iv_remove = itemView.findViewById(R.id.iv_remove)
        }
    }

    //Delete all items from the cart
    @SuppressLint("StaticFieldLeak")
    private inner class GetAsync internal constructor(var menu: Menu, var holder: MyViewHolder) : AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            //Initialize database
            val database = AppDatabase.getInstance(context)
            //Delete all previously added cart items
            database?.menuDao()?.deleteAll()
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            //set restaurant_id of item in cart
            cartRestaurantId = menu.restaurant_id

            //update the changes made
            menu.isOrdered = true
            setVisibility(holder, true)
            ordedItems.add(menu)
            addToCart.update(ordedItems)
        }

    }

    private val ordedItems: ArrayList<Menu?>
    private var cartRestaurantId = 0

    init {
        ordedItems = ArrayList()
    }
}