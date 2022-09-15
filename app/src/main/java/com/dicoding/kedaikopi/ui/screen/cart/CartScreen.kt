package com.dicoding.kedaikopi.ui.screen.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.kedaikopi.di.Injection
import com.dicoding.kedaikopi.model.OrderCoffee
import com.dicoding.kedaikopi.ui.ViewModelFactory
import com.dicoding.kedaikopi.ui.common.UiState
import com.dicoding.kedaikopi.ui.component.ProductCounter

@Composable
fun CartScreen(
    navigateToSuccess: () -> Unit,
    viewModel: CartViewModel = viewModel(
    factory = ViewModelFactory(
        Injection.provideRepository()
    )
),
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.loadCoffeeDrinks()
            }
            is UiState.Success -> {
                BasketSuccessScreen(
                    uiState.data,
                    navigateToSuccess,
                    addCoffeeDrink = {  },
                    removeCoffeeDrink = {  }
                )
            }
            is UiState.Error -> { }
        }
    }
}

@Composable
fun BasketSuccessScreen(
    state: CartState,
    navigateToSuccess: () -> Unit,
    addCoffeeDrink: (Long) -> Unit,
    removeCoffeeDrink: (Long) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar {
            Text(
                text = "Basket",
                modifier = Modifier.padding(horizontal = 12.dp),
                fontSize = 18.sp
            )
        }
        PaymentInfo(
            deliveryCosts = 5,
            total = state.totalPrice,
            currency = '€',
            isPayButtonEnabled = state.orderCoffeeDrinks.isNotEmpty(),
            onPayed = navigateToSuccess
        )
        Spacer(modifier = Modifier.height(8.dp))
        CoffeeDrinkList(
            orderCoffeeDrinks = state.orderCoffeeDrinks,
            onCoffeeDrinkCountIncreased = removeCoffeeDrink,
            onCoffeeDrinkCountDecreased = addCoffeeDrink
        )
    }
}

@Composable
private fun PaymentInfo(
    deliveryCosts: Int,
    total: Int,
    currency: Char,
    isPayButtonEnabled: Boolean,
    onPayed: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        PaymentInfoItem(name = "Delivery Costs:", value = deliveryCosts, currency = currency)
        PaymentInfoItem(name = "Total:", value = total, currency = currency)
        PayButton(isButtonEnabled = isPayButtonEnabled, onPayed = onPayed)
    }
}

@Composable
private fun PaymentInfoItem(
    name: String,
    value: Int,
    currency: Char
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            fontSize = 16.sp
        )

        Text(
            text = "$currency $value",
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            textAlign = TextAlign.End,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun PayButton(
    isButtonEnabled: Boolean,
    onPayed: () -> Unit
) {
    Button(
        enabled = isButtonEnabled,
        onClick = { onPayed() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Pay",
            fontSize = 24.sp
        )
    }
}

@Composable
private fun CoffeeDrinkList(
    orderCoffeeDrinks: List<OrderCoffee>,
    onCoffeeDrinkCountIncreased: (Long) -> Unit,
    onCoffeeDrinkCountDecreased: (Long) -> Unit
) {
    LazyColumn {
        orderCoffeeDrinks.forEach { item ->
            item {
                CoffeeDrinkItem(
                    orderCoffeeDrink = item,
                    onCoffeeDrinkCountIncreased = onCoffeeDrinkCountIncreased,
                    onCoffeeDrinkCountDecreased = onCoffeeDrinkCountDecreased
                )
                Divider()
            }
        }
    }
}

@Composable
private fun CoffeeDrinkItem(
    orderCoffeeDrink: OrderCoffee,
    onCoffeeDrinkCountIncreased: (Long) -> Unit,
    onCoffeeDrinkCountDecreased: (Long) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1.0f)
        ) {
            Text(
                text = orderCoffeeDrink.coffee.title,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = orderCoffeeDrink.coffee.price.toString(),
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            ProductCounter(
                orderId = orderCoffeeDrink.coffee.id,
                orderCount = orderCoffeeDrink.count,
                onProductIncreased = onCoffeeDrinkCountIncreased,
                onProductDecreased = onCoffeeDrinkCountDecreased
            )
        }
    }

}