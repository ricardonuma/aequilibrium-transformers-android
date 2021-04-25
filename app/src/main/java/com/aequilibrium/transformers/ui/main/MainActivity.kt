package com.aequilibrium.transformers.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aequilibrium.transformers.R
import com.aequilibrium.transformers.databinding.MainActivityBinding
import com.aequilibrium.transformers.ui.common.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private lateinit var navController: NavController

    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // set top level destination
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.transformersFragment))

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, _, _ ->
            updateToolbar()
        }

        viewModel.loading.observe(this) {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun updateToolbar() {
        binding.toolbar.setBackgroundColor(Color.WHITE)
        binding.toolbar.visibility = View.VISIBLE
    }
}