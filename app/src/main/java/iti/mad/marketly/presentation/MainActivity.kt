package iti.mad.marketly.presentation


import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import iti.mad.marketly.R


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    lateinit var floatButton: FloatingActionButton
    lateinit var bottomAppBar: BottomAppBar
    lateinit var navHostFragment: NavHostFragment
    lateinit var layoutSpace: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavView)
        bottomAppBar = findViewById(R.id.bottomAppBar)
        floatButton = findViewById(R.id.fab_home)
        layoutSpace = findViewById(R.id.layoutSpace)


        // Set up the Navigation Controller
        /*  navController = findNavController(R.id.navHostFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.productDetailsFragment) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
        }*/
        // another way to get Nav Controller -- this way is using if use fab insing bottom navigation
        // if using previous way when you navigate using fab - bottom navigation not working so you must use this way

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_Host_Fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Set up the BottomNavigationView with the NavController
        bottomNavigationView.setupWithNavController(navController)

        // Add an OnDestinationChangedListener to the NavController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Reset the selected item in the BottomNavigationView when the destination changes
            bottomNavigationView.menu.findItem(destination.id)?.isChecked = true
            if (destination.id == R.id.productDetailsFragment  || destination.id == R.id.cartFragment ) {
                bottomAppBar.visibility = View.GONE
                floatButton.visibility = View.GONE
                layoutSpace.visibility = View.GONE
                changeMarginFragment(-40)

            } else {
                bottomAppBar.visibility = View.VISIBLE
                floatButton.visibility = View.VISIBLE
                layoutSpace.visibility = View.VISIBLE
                changeMarginFragment(80)
            }

        }


        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.categoryFragment,
                R.id.cartFragment,
                R.id.homeFragment,
                R.id.favouriteFragment,
                R.id.myProfile
            )
        )

        floatButton.setOnClickListener {
            navController.navigate(R.id.homeFragment)

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    private fun changeMarginFragment(newValue: Int) {
        var layoutParent: ConstraintLayout = findViewById(R.id.constraintFragment)

        val layoutParams = layoutParent.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.bottomMargin = newValue
        layoutParent.layoutParams = layoutParams
    }



}