package com.example.cupcake

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.data.DataSource
import com.example.cupcake.ui.OrderViewModel
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen
import com.example.cupcake.ui.SummaryScreen
import com.example.cupcake.R

@Composable
fun CupcakeApp() {
    val viewModel = OrderViewModel()
    val navController = rememberNavController()
    
    val currentScreen = navController.currentDestination?.route?.let { route ->
        CupcakeScreen.valueOf(route.uppercase())
    } ?: CupcakeScreen.START
    
    val canNavigateBack = currentScreen != CupcakeScreen.START
    
    Scaffold(
        topBar = {
            CupcakeAppBar(
                canNavigateBack = canNavigateBack,
                navigateUp = navController::navigateUp
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.START.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(CupcakeScreen.START.name) {
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions,
                    onNextButtonClicked = { selectedQuantity ->
                        viewModel.setQuantity(selectedQuantity)
                        navController.navigate(CupcakeScreen.FLAVOR.name)
                    }
                )
            }
            
            composable(CupcakeScreen.FLAVOR.name) {
                SelectOptionScreen(
                    subtotal = viewModel.uiState.value.price,
                    options = viewModel.flavors,
                    onSelectionChanged = { viewModel.setFlavor(it) },
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(CupcakeScreen.START.name, false)
                    },
                    onNextButtonClicked = {
                        navController.navigate(CupcakeScreen.PICKUP_DATE.name)
                    }
                )
            }
            
            composable(CupcakeScreen.PICKUP_DATE.name) {
                SelectOptionScreen(
                    subtotal = viewModel.uiState.value.price,
                    options = viewModel.pickupDates,
                    onSelectionChanged = { viewModel.setDate(it) },
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(CupcakeScreen.START.name, false)
                    },
                    onNextButtonClicked = {
                        navController.navigate(CupcakeScreen.SUMMARY.name)
                    }
                )
            }
            
            composable(CupcakeScreen.SUMMARY.name) {
                SummaryScreen(
                    orderUiState = viewModel.uiState.value,
                    modifier = Modifier.fillMaxWidth(),
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(CupcakeScreen.START.name, false)
                    }
                )
            }
        }
    }
}

@Composable
fun CupcakeAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
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