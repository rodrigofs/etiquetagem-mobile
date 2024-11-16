package com.esoft.emobile.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.esoft.emobile.navigation.AppNavigation
import com.esoft.emobile.support.PermissionManager
import com.esoft.emobile.ui.theme.EMobileTheme
import com.esoft.emobile.ui.views.access.AccessScreen
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

private const val REQUEST_CODE_UPDATE = 100

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionManager: PermissionManager
    private lateinit var appUpdateManager: AppUpdateManager
    private val appUpdateType = AppUpdateType.IMMEDIATE

    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        var isDataLoaded = false

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        permissionManager = PermissionManager(this)

        appUpdateManager = AppUpdateManagerFactory.create(this)

        if (appUpdateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.registerListener(installStateUpdatedListener)
        }

        checkForAppUpdate()

        splashScreen.setKeepOnScreenCondition {
            !isDataLoaded
        }

        mainViewModel.checkAccessStatus { isActivated ->
            isDataLoaded = true

            setContent {

                val navController = rememberNavController()
                val backStackEntryState by navController.currentBackStackEntryAsState()

                EMobileTheme {

                    Surface(
                        color = MaterialTheme.colorScheme.background
                    ) {
                        if (isActivated) {
                            AppNavigation(
                                navController = navController,
                                permissionManager = permissionManager,
                                bluetoothViewModel = mainViewModel

                                //modifier = Modifier.padding(innerPadding)
                            )
                        } else {
                            AccessScreen(navController)
                        }
                    }

                }
            }

        }


    }

    private fun checkForAppUpdate() {
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks whether the platform allows the specified type of update,
        // and current version staleness.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            val isUpdateAvailable =
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (appUpdateType) {
                AppUpdateType.IMMEDIATE -> appUpdateInfo.isImmediateUpdateAllowed
                AppUpdateType.FLEXIBLE -> appUpdateInfo.isFlexibleUpdateAllowed
                else -> false
            }

            if (isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    appUpdateType,
                    // an activity result launcher registered via registerForActivityResult
                    this,
                    // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                    // flexible updates.
                    REQUEST_CODE_UPDATE
                )
            }

        }
    }

    private val installStateUpdatedListener = InstallStateUpdatedListener { installState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            Toast.makeText(
                this,
                "Atualização já está pronta. Reiniciando em 5 segundos",
                Toast.LENGTH_LONG
            ).show()
            lifecycleScope.launch {
                delay(5.seconds)
                appUpdateManager.completeUpdate()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (appUpdateType == AppUpdateType.IMMEDIATE) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        appUpdateType,
                        this,
                        REQUEST_CODE_UPDATE
                    )
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                println("Update flow failed! Result code: $resultCode")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (appUpdateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }
}
