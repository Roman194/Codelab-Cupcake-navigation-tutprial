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
package com.example.cupcake.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cupcake.R
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.components.FormattedPriceLabel
import com.example.cupcake.ui.theme.CupcakeTheme

/**
 * This composable expects [orderUiState] that represents the order state, [onCancelButtonClicked]
 * lambda that triggers canceling the order and passes the final order to [onSendButtonClicked]
 * lambda
 */
@Composable
fun SummaryScreen(
    orderViewModel: OrderViewModel = viewModel(),
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val orderUiState = orderViewModel.uiState.value

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                text = stringResource(R.string.order_summary),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.summary_description),
                style = MaterialTheme.typography.bodyMedium
            )
            Divider(thickness = dimensionResource(R.dimen.thickness_divider))
            Text(
                text = stringResource(R.string.quantity),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = orderUiState.quantity.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Divider(thickness = dimensionResource(R.dimen.thickness_divider))
            Text(
                text = stringResource(R.string.flavor),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = orderUiState.flavor,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Divider(thickness = dimensionResource(R.dimen.thickness_divider))
            Text(
                text = stringResource(R.string.pickup_date),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = orderUiState.date,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Divider(thickness = dimensionResource(R.dimen.thickness_divider))
            FormattedPriceLabel(
                subtotal = orderUiState.price,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onCancelButtonClicked
            ) {
                Text(stringResource(R.string.cancel))
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    // TODO: Implement order confirmation
                    // This would typically navigate back to the start screen
                    // and reset the order
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    }
}

@Preview
@Composable
fun OrderSummaryPreview() {
    CupcakeTheme {
        SummaryScreen(
            orderViewModel = OrderViewModel(),
            onCancelButtonClicked = {},
            modifier = Modifier.fillMaxHeight()
        )
    }
}
