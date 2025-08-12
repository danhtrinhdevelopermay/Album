package com.ios18photos.app

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ios18photos.app.databinding.ActivityMainBinding

/**
 * Main Activity for iOS 18 Photos application
 * Handles navigation, permissions, and theme setup
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Required permissions for accessing media files
    private val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    // Permission launcher for requesting media access
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allPermissionsGranted = permissions.values.all { it }
        if (allPermissionsGranted) {
            setupNavigation()
        } else {
            showPermissionDeniedDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up system UI for iOS-like appearance
        setupSystemUI()

        // Check permissions and setup navigation
        checkPermissionsAndSetup()
    }

    /**
     * Setup system UI for iOS-like appearance with translucent status and navigation bars
     */
    private fun setupSystemUI() {
        // Make status bar translucent
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = ContextCompat.getColor(this, R.color.tab_bar_background)
        
        // Adjust for light/dark theme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isLightTheme = nightModeFlags != Configuration.UI_MODE_NIGHT_YES
            if (isLightTheme) {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = 
                    android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    /**
     * Check required permissions and setup navigation if granted
     */
    private fun checkPermissionsAndSetup() {
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isEmpty()) {
            setupNavigation()
        } else {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    /**
     * Setup navigation controller and bottom navigation
     */
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup bottom navigation
        binding.bottomNavigation.setupWithNavController(navController)

        // Setup top app bar
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_photos,
                R.id.navigation_albums,
                R.id.navigation_search,
                R.id.navigation_for_you
            )
        )
        setSupportActionBar(binding.topAppBar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Update toolbar title based on navigation
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.topAppBar.title = when (destination.id) {
                R.id.navigation_photos -> getString(R.string.library)
                R.id.navigation_albums -> getString(R.string.nav_albums)
                R.id.navigation_search -> getString(R.string.nav_search)
                R.id.navigation_for_you -> getString(R.string.nav_for_you)
                else -> getString(R.string.app_name)
            }
        }
    }

    /**
     * Show dialog when permissions are denied
     */
    private fun showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.permission_required)
            .setMessage(R.string.permission_required)
            .setPositiveButton(R.string.grant_permission) { _, _ ->
                permissionLauncher.launch(requiredPermissions)
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}