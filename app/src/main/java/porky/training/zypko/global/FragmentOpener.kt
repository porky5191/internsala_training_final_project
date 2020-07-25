package porky.training.zypko.global

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import porky.training.zypko.R

class FragmentOpener {

    fun open(fragment: Fragment, fm: FragmentManager, animateLayout: Boolean, addToBackStack: Boolean, clearBackStack: Boolean) {
       //initialize fragment transaction
        val transaction = fm.beginTransaction()
        //clear stack based if sent clearBackStack=true
        clearStack(fm, clearBackStack)
        //show enter & exit animation if sent animateLayout=true
        showAnimateion(transaction, animateLayout)
        //making a transaction
        transaction.replace(R.id.frame_layout, fragment)
        //if send addToBackStack=true add transaction into stack
        addToStack(fragment, transaction, addToBackStack)
        //commit the transaction
        transaction.commit()
    }

    //if container id of framLayout is passd then this methos executes
    fun open(fragment: Fragment?, fm: FragmentManager, containerId: Int) {
        val transaction = fm.beginTransaction()
        transaction.replace(containerId, fragment!!)
        transaction.commit()
    }

    fun clearStack(fm: FragmentManager, clearBackStack: Boolean) {
        if (clearBackStack) {
            //clear the BackStack
            for (i in 0 until fm.backStackEntryCount) fm.popBackStack()
        }
    }

    private fun showAnimateion(transaction: FragmentTransaction, animateLayout: Boolean) {

        if (animateLayout) {
            //show animation while fragment enters & exits
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
        }
    }

    private fun addToStack(fragment: Fragment, transaction: FragmentTransaction, addToBackStack: Boolean) {
        if (addToBackStack) {
            //add the fragment to backStack
            val name = fragment.javaClass.name.toUpperCase()
            transaction.addToBackStack(name)
        }
    }
}