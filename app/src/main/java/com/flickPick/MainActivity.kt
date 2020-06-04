package com.flickPick

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.flickPick.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration : AppBarConfiguration
    private var clicked = false

    override fun onBackPressed() {
        Log.i("FOC:", "ON BACK PRESSED ACTIVITY")
        val fragmentList: List<*> = supportFragmentManager.fragments
        val list = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container)!!.childFragmentManager.fragments
        Log.i("BCK:", list.toString())
        var handled = false
        for (f in list) {
            Log.i("BCK:", "${f.toString()} - ${f is BackHandlerFragment}")
            if (f is BackHandlerFragment) {
                handled = f.onBackPressed()
                if (handled) break
            }
        }
        if (!handled) {
            if (clicked) super.onBackPressed()
            else {
                clicked = true
                Timer().schedule(1500) {
                    clicked = false
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.nav_host_fragment_container)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
//        navController.addOnDestinationChangedListener(object: NavController.OnDestinationChangedListener{
//            override fun onDestinationChanged(
//                controller: NavController,
//                destination: NavDestination,
//                arguments: Bundle?
//            ) {
//                controller.popBackStack(destination.parent!!.id, true)
//            }
//        })
    }

    //TODO do not replace hamburger with up arrow - top rated & films are equivalent
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment_container)
        return NavigationUI.navigateUp(navController, drawerLayout)
//        return super.onSupportNavigateUp()

    }

}

open class BackHandlerFragment : Fragment() {
    /**
     * Could handle back press.
     * @return true if back press was handled
     */
    open fun onBackPressed(): Boolean {
        return false
    }
}