/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.data.DataSource
import com.example.cupcake.ui.OrderViewModel
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen

//Cilo code 1st prompt 1st attempt
/**
 * enum class that represents the screens in the cupcake ordering flow
 */
enum class CupcakeScreen {
    StartOrder,
    Flavor,
    Date,
    Summary
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CupcakeAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    currentScreen: CupcakeScreen = CupcakeScreen.StartOrder
) {
    TopAppBar(
        title = {
            Text(
                text = when (currentScreen) {
                    CupcakeScreen.StartOrder -> stringResource(R.string.order_cupcakes)
                    CupcakeScreen.Flavor -> stringResource(R.string.choose_flavor)
                    CupcakeScreen.Date -> stringResource(R.string.choose_pickup_date)
                    CupcakeScreen.Summary -> stringResource(R.string.order_summary)
                }
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CupcakeApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Determine the current screen based on NavController's back stack
    val currentScreen = when (navController.currentDestination?.route) {
        CupcakeScreen.Flavor.name -> CupcakeScreen.Flavor
        CupcakeScreen.Date.name -> CupcakeScreen.Date
        CupcakeScreen.Summary.name -> CupcakeScreen.Summary
        else -> CupcakeScreen.StartOrder
    }

    // Check if back navigation is possible (not on StartOrder screen)
    val canNavigateBack = currentScreen != CupcakeScreen.StartOrder

    // Private function to cancel order and navigate to start
    fun cancelOrderAndNavigateToStart() {
        viewModel.resetOrder()
        navController.popBackStack(CupcakeScreen.StartOrder.name, inclusive = false)
    }

    Scaffold(
        topBar = {
            CupcakeAppBar(
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.navigateUp() },
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.StartOrder.name,
            modifier = Modifier
        ) {
            composable(CupcakeScreen.StartOrder.name) {
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions,
                    onNextButtonClicked = { quantity ->
                        viewModel.setQuantity(quantity)
                        navController.navigate(CupcakeScreen.Flavor.name)
                    },
                    modifier = Modifier
                )
            }
            composable(CupcakeScreen.Flavor.name) {
                val flavors = DataSource.flavors.map { stringResource(it) }
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = flavors,
                    onSelectionChanged = { flavor ->
                        viewModel.setFlavor(flavor)
                    },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart()
                    },
                    onNextButtonClicked = {
                        navController.navigate(CupcakeScreen.Date.name)
                    },
                    modifier = Modifier
                )
            }
            composable(CupcakeScreen.Date.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = uiState.pickupOptions,
                    onSelectionChanged = { date ->
                        viewModel.setDate(date)
                    },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart()
                    },
                    onNextButtonClicked = {
                        navController.navigate(CupcakeScreen.Summary.name)
                    },
                    modifier = Modifier
                )
            }
            composable(CupcakeScreen.Summary.name) {
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart()
                    },
                    onSendButtonClicked = {
                        // TODO: Implement order confirmation functionality
                    },
                    modifier = Modifier
                )
            }
        }
    }
}
