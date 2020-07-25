package porky.training.zypko.util

import android.widget.EditText

class RegisterUtil {
    fun verify(name: EditText, email: EditText, mobileNo: EditText, address: EditText,
               password: EditText, confirmPass: EditText): Boolean {
        //get String from editText
        val pass = password.text.toString()
        val conf = confirmPass.text.toString()
        val mailAdd = email.text.toString()

        //set all error to null
        name.error = null
        email.error = null
        mobileNo.error = null
        address.error = null
        password.error = null
        confirmPass.error = null

        //check various conditions & validate fields
        if (name.text.toString().length < 3) {
            name.error = "minimum length must be 3"
            return false
        } else if (mailAdd.isEmpty()) {
            email.error = "email can't be empty"
            return false
        } else if (mobileNo.length() != 10) {
            mobileNo.error = "must have 10 digits"
            return false
        } else if (address.text.toString().isEmpty()) {
            address.error = "address can't be empty"
            return false
        } else if (pass.isEmpty() || pass.length < 6) {
            password.error = "incorrect password"
            return false
        } else if (conf.isEmpty() || conf.length < 6) {
            confirmPass.error = "incorrect password"
            return false
        } else if (pass != conf) {
            confirmPass.error = "both password doesn't match"
            return false
        }
        return true
    }
}