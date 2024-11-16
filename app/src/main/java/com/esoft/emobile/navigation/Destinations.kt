package com.esoft.emobile.navigation

sealed class AppDestination(val route: String) {
    data object TagGenerate : AppDestination("tag_generate")
    data object ConnectPrint : AppDestination("connect_print")
    data object Access : AppDestination("access")
}