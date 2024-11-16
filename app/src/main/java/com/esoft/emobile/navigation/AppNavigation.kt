package com.esoft.emobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.esoft.emobile.support.PermissionManager
import com.esoft.emobile.ui.MainActivityViewModel
import com.esoft.emobile.ui.customization.ui.customization.GlobalSnackbarScreen
import com.esoft.emobile.ui.views.printer.BluetoothScreen
import com.esoft.emobile.ui.views.tag.PrintTagScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bluetoothViewModel: MainActivityViewModel,
    permissionManager: PermissionManager
) {

    GlobalSnackbarScreen { showSnackbar ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.ConnectPrint.route,
            modifier = modifier
        ) {
            composable(AppDestination.ConnectPrint.route) {
                BluetoothScreen(
                    navController = navController,
                    permissionManager = permissionManager,
                    bluetoothViewModel = bluetoothViewModel,
                    onTagPrint = {
                        navController.navigate(AppDestination.TagGenerate.route){
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }

            composable(AppDestination.TagGenerate.route) {
                PrintTagScreen(
                    permissionManager = permissionManager,
                    bluetoothViewModel = bluetoothViewModel,
                    navController = navController,
                    onConnectPrint = {
                        navController.navigate(AppDestination.ConnectPrint.route){
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
        }


    }
}

