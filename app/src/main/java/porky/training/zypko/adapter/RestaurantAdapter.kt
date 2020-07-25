package porky.training.zypko.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import porky.training.zypko.R
import porky.training.zypko.fragment.home.FragMenu
import porky.training.zypko.global.AppDatabase
import porky.training.zypko.global.FragmentOpener
import porky.training.zypko.model.Restaurant
import java.util.*

class RestaurantAdapter(private val context: Context, private val fm: FragmentManager, private val arrayList: ArrayList<Restaurant?>?) : RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater
                .from(context)
                .inflate(R.layout.recycler_restaurant, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val restaurant = arrayList?.get(position)
        holder.l_hotenName.text = restaurant?.name ?: ""
        holder.l_cost.text = restaurant?.costForOne.toString() + " /per person"
        holder.l_rating.text = restaurant?.rating.toString()

        //set icon based on restaurant is favourite or not
        if (restaurant!!.isFavourite) holder.iv_favourite.setImageResource(R.drawable.icon_love_fill) else holder.iv_favourite.setImageResource(R.drawable.icon_love)
        Picasso.get()
                .load(restaurant.imageUrl)
                .resize(90, 90)
                .centerCrop()
                .into(holder.iv_hotel)

        //set the change of icon when love icon is clicked
        holder.iv_favourite.setOnClickListener { v: View? ->
            if (restaurant.isFavourite) {
                //change in dataset
                restaurant.isFavourite = false
                //change the icon
                holder.iv_favourite.setImageResource(R.drawable.icon_love)

                //remove the restaurant from loclaDV
                GetLocalData().init(restaurant, false, true).execute()
            } else {
                //change the dataset
                restaurant.isFavourite = true
                //change the icon
                holder.iv_favourite.setImageResource(R.drawable.icon_love_fill)

                //add the restaurant to localDb
                GetLocalData().init(restaurant, true, false).execute()
            }
        }

        //handle click event when a restaurant is clicke
        holder.ll_restaurant.setOnClickListener { v: View? ->
            //move to menu fragment using opener class
            opener.open(FragMenu(restaurant), fm, false, true, false)
        }
    }

    override fun getItemCount(): Int {
        return arrayList?.size ?: 0
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_hotel: ImageView
        var iv_favourite: ImageView
        var l_hotenName: TextView
        var l_cost: TextView
        var l_rating: TextView
        var ll_restaurant: LinearLayout

        init {
            iv_hotel = itemView.findViewById(R.id.iv_hotel)
            iv_favourite = itemView.findViewById(R.id.iv_favourite)
            l_hotenName = itemView.findViewById(R.id.l_hotenName)
            l_cost = itemView.findViewById(R.id.l_cost)
            l_rating = itemView.findViewById(R.id.l_rating)
            ll_restaurant = itemView.findViewById(R.id.ll_restaurant)
        }
    }

    //TODO:(ASYNC TASK) Initialize Asynctask to add/delete item from localDb
    @SuppressLint("StaticFieldLeak")
    private inner class GetLocalData : AsyncTask<Void?, Void?, Void?>() {
        private var insert = false
        private var delete = false
        private var restaurant: Restaurant? = null
        private var database: AppDatabase? = null

        //Initialize different data
        fun init(restaurant: Restaurant?, insert: Boolean, remove: Boolean): GetLocalData {
            this.insert = insert
            delete = remove
            this.restaurant = restaurant
            database = AppDatabase.getInstance(context)
            return this
        }

        override fun doInBackground(vararg params: Void?): Void? {
            if (insert) {
                //add the restaurant to localDb
                database!!.restaurantDao()!!.insert(restaurant)
            } else if (delete) {
                //remove the restaurant from localDb
                database!!.restaurantDao()?.delete(restaurant)
            }
            return null;
        }
    }

    private val opener: FragmentOpener

    init {
        opener = FragmentOpener()
    }
}