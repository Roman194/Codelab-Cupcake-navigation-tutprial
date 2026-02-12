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
import androidx.compose.foundation.layout.padding
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
import com.example.cupcake.ui.OrderViewModel
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen

//GigaCode 1st prompt 3rd attempt
/**
 * Код всё ещё не запускается. Уточни пожалуйста ещё раз все возможные значения,
 * которые может передать uiState как переменная типа OrderUiState.
 * Также передай в CupcakeAppBar параметр currentScreen и сделай так, чтобы title выводил
 * именно значение currentScreen.title
 */

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@Composable
fun CupcakeAppBar(
    currentScreen: CupcakeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
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

/**
 * Enum class for Cupcake screens
 */
enum class CupcakeScreen(val title: Int) {
    START(R.string.app_name),
    FLAVOR(R.string.choose_flavor),
    PICKUP(R.string.choose_pickup_date),
    SUMMARY(R.string.order_summary)
}

@Composable
fun CupcakeApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val canNavigateBack = navController.previousBackStackEntry != null

    Scaffold(
        topBar = {
            val currentScreen by navController.currentBackStackEntryFlow.collectAsState()
            val currentCupcakeScreen = CupcakeScreen.valueOf(currentScreen.destination.route ?: "")
            CupcakeAppBar(
                currentScreen = currentCupcakeScreen,
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.START.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(CupcakeScreen.START.name) {
                StartOrderScreen(
                    onSubmitClicked = { quantity ->
                        viewModel.setQuantity(quantity)
                        navController.navigate(CupcakeScreen.FLAVOR.name)
                    }
                )
            }

            composable(CupcakeScreen.FLAVOR.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = uiState.possibleFlavors,
                    onSelectionChanged = { selectedOption ->
                        viewModel.setFlavor(selectedOption)
                    },
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    onNextButtonClicked = {
                        navController.navigate(CupcakeScreen.PICKUP.name)
                    }
                )
            }

            composable(CupcakeScreen.PICKUP.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = uiState.possibleDates,
                    onSelectionChanged = { selectedOption ->
                        viewModel.setDate(selectedOption)
                    },
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    onNextButtonClicked = {
                        navController.navigate(CupcakeScreen.SUMMARY.name)
                    }
                )
            }

            composable(CupcakeScreen.SUMMARY.name) {
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    onSendButtonClicked = { /* TODO: send order */ }
                )
            }
        }
    }
}

private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(CupcakeScreen.START.name, inclusive = false)
}