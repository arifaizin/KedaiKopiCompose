package com.dicoding.kedaikopi.ui.screen.cart

import OrderButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                CartSuccessScreen(
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
fun CartSuccessScreen(
    state: CartState,
    navigateToSuccess: () -> Unit,
    addCoffeeDrink: (Long) -> Unit,
    removeCoffeeDrink: (Long) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(backgroundColor = Color.White) {
            Text(
                text = "Keranjang",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
        OrderButton(
            text = "Total Pesanan : ${state.totalPrice}",
            enabled = state.orderCoffeeDrinks.isNotEmpty(),
            onClick = navigateToSuccess,
            modifier = Modifier.padding(16.dp)
        )
        CoffeeDrinkList(
            orderCoffeeDrinks = state.orderCoffeeDrinks,
            onCoffeeDrinkCountIncreased = removeCoffeeDrink,
            onCoffeeDrinkCountDecreased = addCoffeeDrink,
        )
    }
}

@Composable
private fun CoffeeDrinkList(
    orderCoffeeDrinks: List<OrderCoffee>,
    onCoffeeDrinkCountIncreased: (Long) -> Unit,
    onCoffeeDrinkCountDecreased: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
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
        Image(
            painter = painterResource(orderCoffeeDrink.coffee.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(AbsoluteRoundedCornerShape(8.dp))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1.0f)
        ) {
            Text(
                text = orderCoffeeDrink.coffee.title,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Text(
                text = (orderCoffeeDrink.coffee.price * orderCoffeeDrink.count).toString(),
                style = MaterialTheme.typography.subtitle2
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