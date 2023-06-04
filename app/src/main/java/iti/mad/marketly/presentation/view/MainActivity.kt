package iti.mad.marketly.presentation.view

import iti.mad.marketly.R


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigationView = findViewById(R.id.bottomNavView)

        // Set up the Navigation Controller
        navController = findNavController(R.id.navHostFragment)
        /*
                another way to get Nav Controller

                val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
                navController = navHostFragment.navController
                */
        // Set up the Bottom Navigation View with the Navigation Controller

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment,R.id.cartFragment,R.id.categoryFragment,R.id.favouriteFragment,R.id.profileFragment)
        )
        // setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)



    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}