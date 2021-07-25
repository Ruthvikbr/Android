package com.example.jetpacknavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.my_nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(
            navController,
            appBarConfiguration
        )
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
    }
}