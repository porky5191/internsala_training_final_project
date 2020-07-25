package porky.training.zypko.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import porky.training.zypko.R
import porky.training.zypko.activity.MainActivity
import porky.training.zypko.fragment.home.FragRestaurants
import porky.training.zypko.fragment.navigation.FragFAQ
import porky.training.zypko.fragment.navigation.FragFavRestaurants
import porky.training.zypko.fragment.navigation.FragOrderHistory
import porky.training.zypko.fragment.navigation.FragProfile
import porky.training.zypko.global.*
import porky.training.zypko.interfaces.DrawerLocker
import porky.training.zypko.interfaces.OnAlertClick
import porky.training.zypko.interfaces.UserInterface

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DrawerLocker {

    companion object {
        var navigationView: NavigationView? = null
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        //Initialize fragment manager
        fragmentManager = supportFragmentManager

        //Initalize fragment opener to open fragment
        fragmentOpener = FragmentOpener()
        //Initialize dialog builder to show dialog
        dialogBuilder = DialogBuilder()

        //Initialize connection object
        checkConnection = CheckConnection(this)

        //initialize the fragment opener class
        val opener = FragmentOpener()

        //TODO: Navigation Drawer
        //initialize the toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Initialize navigationView And set onclick listenere to it
        navigationView = findViewById(R.id.nav_view)
        val navView = navigationView
        if (navView != null){
            navView.setNavigationItemSelectedListener(this)
        }


        //initialize the drawer & set properties to it
        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        val drawerLocal = drawer
        if (drawerLocal != null){
            drawerLocal.addDrawerListener(toggle!!)
        }
        toggle!!.syncState()

        //open fragment
        if (savedInstanceState == null) {
            opener.open(FragRestaurants(), supportFragmentManager, false, false, false)
            //by default first item will be selected always
            if (navView != null)
                navView.setCheckedItem(R.id.nav_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> fragmentOpener!!.clearStack(fragmentManager!!, true)
            R.id.nav_profile -> fragmentOpener!!.open(FragProfile(), fragmentManager!!, false, true, true)
            R.id.nav_fav_restaurant -> fragmentOpener!!.open(FragFavRestaurants(), fragmentManager!!, false, true, true)
            R.id.nav_order_history -> if (checkConnection!!.hasConnection()) fragmentOpener!!.open(FragOrderHistory(), fragmentManager!!, false, true, true) else openInternet()
            R.id.nav_faq -> fragmentOpener!!.open(FragFAQ(), fragmentManager!!, false, true, true)
            R.id.nav_logout -> doLogout()
        }

        //Close the drawer once a drawer item is selected
        drawer!!.closeDrawer(GravityCompat.START)

        //if false means no item selected even if we have clicked
        return true
    }

    private fun openInternet() {
        DialogBuilder().init(this)
                .setTitle("No Internet")
                .setMessage("You have no proper internet connection. Click ok to open internet settings")
                .setPositiveBtn("Ok", object : OnAlertClick {
                    override fun onClick() {
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }
                })
                .setNegativeBtn("Cancel", object : OnAlertClick {
                    override fun onClick() {

                    }
                })
                .build()
    }

    private fun doLogout() {
        dialogBuilder!!.init(this).setTitle("Confirmation")
                .setMessage("Are you sure want to logout?")
                .setPositiveBtn("YES", object : OnAlertClick {
                    override fun onClick() {
                        //initialize SharedPreference
                        val editor = PreferenceEditor(this@MainActivity).init()
                        //put the userId to 0
                        editor.put(UserInterface.USER_ID, 0)
                        //delete the cart items & favourite restaurant
                        GetAsync().execute()
                        //move to StartActivity
                        startActivity(Intent(this@MainActivity, StartActivity::class.java))
                        finish()
                    }
                })
                .setNegativeBtn("NO", object : OnAlertClick {
                    override fun onClick() {

                    }
                })
                .setCelable(false)
                .build()
    }

    override fun setDrawerEnable(enable: Boolean) {
        //check whether drawer tho show or not
        val lockMode = if (enable) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        drawer!!.setDrawerLockMode(lockMode)
        //toggle button enable/disable
        toggle!!.isDrawerIndicatorEnabled = enable
    }

    override fun onBackPressed() {
        //on pressing Back button
        //If Drawer is open -> close it
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }


    @SuppressLint("StaticFieldLeak")
    internal inner class GetAsync : AsyncTask<Void?, Void?, Void?>() {
        var database: AppDatabase? = null
        override fun doInBackground(vararg voids: Void?): Void? {
            Log.d("MyResponse_", "items deleted successfully")

            //initialize the database
            database = AppDatabase.getInstance(this@MainActivity)
            //delete all food items previously added to the cart from the localDatabase
            val localDb = database
            localDb!!.menuDao()!!.deleteAll()
            localDb.restaurantDao()!!.deleteAll()
            return null
        }
    }

    //----------------------------------------------
    private var drawer: DrawerLayout? = null
    private var toggle: ActionBarDrawerToggle? = null
    private var fragmentOpener: FragmentOpener? = null
    private var dialogBuilder: DialogBuilder? = null
    private var fragmentManager: FragmentManager? = null
    private var checkConnection: CheckConnection? = null

}