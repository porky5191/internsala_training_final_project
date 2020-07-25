package porky.training.zypko.pref

import android.content.Context
import porky.training.zypko.global.PreferenceEditor
import porky.training.zypko.interfaces.GlobalInterface
import porky.training.zypko.interfaces.UserInterface
import porky.training.zypko.model.User

class UserPreference(context: Context?) : UserInterface, GlobalInterface {

    var user: User

        //get all the details of user from SharedPreference
        get() {
            val user = User()
            user.userId = editor[UserInterface.USER_ID, 0]
            user.name = editor[UserInterface.NAME, GlobalInterface.STRING_DEFAULT_VALUE]
            user.mobileNo = editor[UserInterface.MOBILE_NO, GlobalInterface.STRING_DEFAULT_VALUE]
            user.password = editor[UserInterface.PASSWORD, GlobalInterface.STRING_DEFAULT_VALUE]
            user.address = editor[UserInterface.ADDRESS, GlobalInterface.STRING_DEFAULT_VALUE]
            user.email = editor[UserInterface.EMAIL, GlobalInterface.STRING_DEFAULT_VALUE]
            return user
        }

        //save all the details of user to SharedPreference
        set(user) {
            editor.put(UserInterface.USER_ID, user.userId)
                    .put(UserInterface.NAME, user.name)
                    .put(UserInterface.MOBILE_NO, user.mobileNo)
                    .put(UserInterface.PASSWORD, user.password)
                    .put(UserInterface.ADDRESS, user.address)
                    .put(UserInterface.EMAIL, user.email)
        }

    //--------------------------------------------
    private val editor: PreferenceEditor

    init {
        editor = PreferenceEditor(context!!).init()
    }
}