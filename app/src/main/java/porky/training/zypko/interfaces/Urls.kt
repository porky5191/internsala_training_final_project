package porky.training.zypko.interfaces

interface Urls {
    companion object {
        const val MY_TOKEN = "01918f13aeba66"
        const val ALL_RESTAURANTS = "http://13.235.250.119/v2/restaurants/fetch_result/"
        const val MENU = ALL_RESTAURANTS
        const val PLACE_ORDER = "http://13.235.250.119/v2/place_order/fetch_result/"
        const val ORDER_HISTORY = "http://13.235.250.119/v2/orders/fetch_result/"

        /*--------ALl login related urls-----------*/
        const val REGISTER = "http://13.235.250.119/v2/register/fetch_result"
        const val LOGIN = "http://13.235.250.119/v2/login/fetch_result"
        const val RESET_PASSWORD = "http://13.235.250.119/v2/reset_password/fetch_result"
        const val FORGOT_PASSWORD = "http://13.235.250.119/v2/forgot_password/fetch_result"
    }
}