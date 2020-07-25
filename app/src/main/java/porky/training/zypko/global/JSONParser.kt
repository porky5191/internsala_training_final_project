package porky.training.zypko.global

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import porky.training.zypko.interfaces.*
import porky.training.zypko.model.*
import java.util.*

class JSONParser : GlobalInterface {
    //parse the json & return the success
    fun getSuccess(`object`: JSONObject): Boolean {
        var success = false
        try {
            //get the value of outer data
            val object1 = `object`[GlobalInterface.DATA] as JSONObject
            //get the value of success
            success = object1.getBoolean(GlobalInterface.SUCCESS)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return success
    }

    //parse the json & return the JsonArray from the Json response
    fun getArray(`object`: JSONObject): JSONArray {
        var array = JSONArray()
        try {
            //get the data from the response
            val object1 = `object`[GlobalInterface.DATA] as JSONObject
            //get success from response
            val success = object1.getBoolean(GlobalInterface.SUCCESS)
            //check if success is true
            if (success) {
                //get the main body or expected response
                array = object1.getJSONArray(GlobalInterface.DATA)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return array
    }

    //parse the json & return the JsonObject from the Json response
    fun getObject(`object`: JSONObject): JSONObject {
        var jsonObject = JSONObject()
        try {
            val object1 = `object`[GlobalInterface.DATA] as JSONObject
            val success = object1.getBoolean(GlobalInterface.SUCCESS)
            if (success) {
                jsonObject = object1.getJSONObject(GlobalInterface.DATA)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject
    }

    //Parse the data the Data part of the json and put it into a arrayList
    fun parseRestaurant(array: JSONArray): ArrayList<Restaurant?>? {
        val list = ArrayList<Restaurant?>()
        try {
            //loop throughout the JSONArray
            for (i in 0 until array.length()) {
                //Initialize a Restaurant model
                val restaurant = Restaurant()

                //get the JSONObject present at position 'i'
                val jsonObject = array[i] as JSONObject
                //parse the different value present at JSONObject
                //put the values in respective Restaurant model
                restaurant.id = jsonObject.getInt(RestaurantInterface.ID)
                restaurant.name = jsonObject.getString(RestaurantInterface.NAME)
                restaurant.imageUrl = jsonObject.getString(RestaurantInterface.IMAGE_URL)
                restaurant.costForOne = jsonObject.getDouble(RestaurantInterface.COST_FOR_ONE)
                restaurant.rating = jsonObject.getDouble(RestaurantInterface.RATING)
                //add the restaurant model to the ArrayList
                list.add(restaurant)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return list
    }

    //Parse the Menu of a restaurant
    fun parseMenu(array: JSONArray): ArrayList<Menu?>? {
        val list = ArrayList<Menu?>()
        try {
            for (i in 0 until array.length()) {
                val menu = Menu()
                val jsonObject = array[i] as JSONObject
                menu.id = jsonObject.getInt(MenuInterface.ID)
                menu.restaurant_id = jsonObject.getInt(MenuInterface.RESTAURANT_ID)
                menu.costOfOne = jsonObject.getDouble(MenuInterface.COST_FOR_ONE)
                menu.name = jsonObject.getString(MenuInterface.NAME)
                menu.isOrdered = false
                list.add(menu)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return list
    }

    //Parse User
    fun parseUser(`object`: JSONObject): User {
        val user = User()
        try {
            user.userId = `object`.getInt(UserInterface.USER_ID)
            user.name = `object`.getString(UserInterface.NAME)
            user.mobileNo = `object`.getString(UserInterface.MOBILE_NO)
            user.address = `object`.getString(UserInterface.ADDRESS)
            user.email = `object`.getString(UserInterface.EMAIL)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return user
    }

    //parse order history
    fun parseOrderHistory(jsonArray: JSONArray): ArrayList<OrderHistory?>? {
        val list = ArrayList<OrderHistory?>()
        try {
            for (i in 0 until jsonArray.length()) {
                val `object` = jsonArray.getJSONObject(i)
                val history = OrderHistory()
                history.orderId = `object`.getInt(OrderHistoryInterface.ORDER_ID)
                history.restaurantName = `object`.getString(OrderHistoryInterface.RESTAURANT_NAME)
                history.totalCost = `object`.getDouble(OrderHistoryInterface.TOTAL_COST)
                history.orderPlacedAt = `object`.getString(OrderHistoryInterface.ORDER_PLACED_AT)
                history.foodList = parseOrderedFood(`object`.getJSONArray(OrderHistoryInterface.FOOD_ITEMS))
                list.add(history)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return list
    }

    //Parse orderd food
    private fun parseOrderedFood(array: JSONArray): ArrayList<OrderedFood> {
        val foodList = ArrayList<OrderedFood>()
        try {
            for (i in 0 until array.length()) {
                val `object` = array.getJSONObject(i)
                val orderedFood = OrderedFood()
                orderedFood.foodItemId = `object`.getInt(OrderHistoryInterface.FOOD_ITEM_ID)
                orderedFood.foodName = `object`.getString(OrderHistoryInterface.FOOD_NAME)
                orderedFood.cost = `object`.getDouble(OrderHistoryInterface.FOOD_COST)
                foodList.add(orderedFood)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return foodList
    }
}