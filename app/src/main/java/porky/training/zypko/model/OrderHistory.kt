package porky.training.zypko.model

import java.util.*

class OrderHistory {
    var orderId = 0
    var restaurantName: String? = null
    var totalCost = 0.0
    var orderPlacedAt: String? = null
    var foodList: ArrayList<OrderedFood>? = null

}