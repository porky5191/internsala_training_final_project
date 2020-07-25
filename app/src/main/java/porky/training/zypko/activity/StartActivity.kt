package porky.training.zypko.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import porky.training.zypko.R
import porky.training.zypko.fragment.login.FragSplash
import porky.training.zypko.global.FragmentOpener

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        init()
    }

    private fun init() {
        opener = FragmentOpener()
        //open splash screen as soon as the current Activity opens
        opener!!.open(FragSplash(), supportFragmentManager, false, false, false)
    }

    var opener: FragmentOpener? = null
}