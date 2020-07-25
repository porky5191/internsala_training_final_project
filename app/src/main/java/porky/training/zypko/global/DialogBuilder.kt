package porky.training.zypko.global

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import porky.training.zypko.interfaces.OnAlertClick

class DialogBuilder {
    fun init(context: Context?): DialogBuilder {
        //initialize a AlerDialog builder
        builder = AlertDialog.Builder(context)
        return this
    }

    fun setMessage(message: String?): DialogBuilder {
        //set message to the ALertDialog
        builder!!.setMessage(message)
        return this
    }

    fun setTitle(title: String?): DialogBuilder {
        //set title of the AlertDialog
        builder!!.setTitle(title)
        return this
    }

    fun setCelable(canCancel: Boolean): DialogBuilder {
        //set if AlertDialog can be canceled by clicking on the other part of the ui
        builder!!.setCancelable(canCancel)
        return this
    }

    fun setPositiveBtn(btnName: String?, onAlertClick: OnAlertClick): DialogBuilder {
        //set name of positive button & handle it
        builder!!.setPositiveButton(btnName) { dialog: DialogInterface?, which: Int -> onAlertClick.onClick() }
        return this
    }

    fun setNegativeBtn(btnName: String?, onAlertClick: OnAlertClick): DialogBuilder {
        //set name of negative button & handle it
        builder!!.setNegativeButton(btnName) { dialog: DialogInterface?, which: Int -> onAlertClick.onClick() }
        return this
    }

    fun build() {
        //show the ALertDialog
        builder!!.create().show()
    }

    //------------------------------------------
    private var builder: AlertDialog.Builder? = null
    private val onAlertClick: OnAlertClick? = null
}