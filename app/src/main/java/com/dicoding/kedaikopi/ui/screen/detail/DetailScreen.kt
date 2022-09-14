package com.dicoding.kedaikopi.ui.screen.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.kedaikopi.R
import com.dicoding.kedaikopi.data.CoffeeRepository
import com.dicoding.kedaikopi.ui.ViewModelFactory
import com.dicoding.kedaikopi.ui.common.UiState
import com.dicoding.kedaikopi.ui.theme.CoffeeBrown
import com.dicoding.kedaikopi.ui.theme.KedaiKopiTheme
import com.dicoding.kedaikopi.ui.theme.LightGray
import java.text.NumberFormat
import java.util.*

@Composable
fun DetailScreen(
    coffeeId: Long,
    viewModel: DetailFavoriteViewModel = viewModel(
        factory = ViewModelFactory(
            CoffeeRepository()
        )
    ),
    onBackClick: () -> Unit
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getCoffeeById(coffeeId)
            }
            is UiState.Success -> {
                val menu = uiState.data
                DetailContent(
                    menu.image,
                    menu.title,
                    menu.price,
                    onBackClick = onBackClick
                )
            }
            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailContent(
    @DrawableRes image: Int,
    title: String,
    basePrice: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var totalPrice by rememberSaveable() { mutableStateOf(0) }
    var optionPrice by rememberSaveable() { mutableStateOf(0) }
    var toppingPrice by rememberSaveable() { mutableStateOf(0) }

    Column {
        DetailMenu(modifier, image, title, basePrice, onBackClick = onBackClick)
        Spacer(modifier = Modifier.fillMaxWidth().height(6.dp).background(LightGray))
        val iceOptions = listOf("Normal", "Less", "No Ice")
        var selectedItem by remember { mutableStateOf(iceOptions[0]) }

        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Ice Level",
                fontWeight = FontWeight.ExtraBold,
            )
            Spinner(
                items = iceOptions,
                selectedItem = selectedItem,
                onItemSelected = {
                    selectedItem = it
                },
            )
        }
        Text(
            text = "Topping",
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(16.dp)
        )
        LazyVerticalGrid(
            cells = GridCells.Adaptive(120.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {

            items(dummyTopping) { topping ->
                ToppingItem(topping.image, topping.title, topping.price,
                    onItemSelected = {

                    })
            }
        }

        val totalPrice = basePrice + optionPrice + toppingPrice
        Button(
            onClick = {}
        ) {
            Text("Tambah ke keranjang - $totalPrice")
        }
    }
}

@Composable
private fun DetailMenu(
    modifier: Modifier,
    @DrawableRes image: Int,
    title: String,
    basePrice: Int,
    onBackClick: () -> Unit,
) {
    Box {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.height(196.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
        )
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.padding(16.dp).clickable { onBackClick() }
        )
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.ExtraBold
            )
        )
        Text(
            text = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(basePrice),
            style = MaterialTheme.typography.subtitle2,
            color = CoffeeBrown
        )
        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            style = MaterialTheme.typography.body2,
        )
    }
}

@Composable
fun Spinner(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
) {
    var expanded: Boolean by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        Row(
            modifier = modifier
                .wrapContentSize()
                .clickable { expanded = true }
                .padding(8.dp)
        ) {
            Text(selectedItem)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "drop down arrow"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize()
        ) {
            items.forEachIndexed { index, element ->
                DropdownMenuItem(onClick = {
                    onItemSelected(items[index])
                    expanded = false
                }) {
                    Text(text = element)
                }
            }
        }
    }
}


@Composable
fun ToppingItem(
    @DrawableRes image: Int,
    toppingName: String,
    price: Int,
    isSelected: Boolean = false,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = if (isSelected) Modifier.border(
        2.dp,
        CoffeeBrown,
        RoundedCornerShape(8.dp)
    ) else Modifier
) {
    var checkedState by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.size(width = 98.dp, height = 83.dp)
            .border(if (checkedState) 2.dp else -1.dp, CoffeeBrown, RoundedCornerShape(8.dp))
            .selectable(isSelected) {
                checkedState = !checkedState
                onItemSelected(price)
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )
            Text(
                text = toppingName,
                style = MaterialTheme.typography.body2,
            )
        }
    }
}

data class Topping(
    val image: Int,
    val title: String,
    val price: Int,
)

val dummyTopping = listOf(
    Topping(R.drawable.topping_biscuit, "Biscuit", 18000),
    Topping(R.drawable.topping_caramel, "Caramel", 6000),
    Topping(R.drawable.topping_caramel, "Caramel", 6000),
    Topping(R.drawable.topping_caramel, "Caramel", 6000),
    Topping(R.drawable.topping_caramel, "Caramel", 6000),
    Topping(R.drawable.topping_caramel, "Caramel", 6000),
    Topping(R.drawable.topping_caramel, "Caramel", 6000),
)

@Preview(showBackground = true)
@Composable
fun ToppingPreview() {
    KedaiKopiTheme {
        val topping = dummyTopping[0]
        ToppingItem(topping.image, topping.title, topping.price, true, onItemSelected = {

        })
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    KedaiKopiTheme {
        DetailContent(R.drawable.menu1, "title", 30000, onBackClick = {})
    }
}