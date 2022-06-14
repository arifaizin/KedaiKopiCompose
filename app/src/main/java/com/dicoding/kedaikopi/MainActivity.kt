package com.dicoding.kedaikopi

import android.os.Bundle
import android.view.MenuItem
import android.widget.GridView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.kedaikopi.ui.theme.KedaiKopiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

private val filterData = listOf(
    R.drawable.icon_category_all to R.string.category_all,
    R.drawable.icon_category_americano to R.string.category_americano,
    R.drawable.icon_category_cappuccino to R.string.category_cappuccino,
    R.drawable.icon_category_espresso to R.string.category_espresso,
    R.drawable.icon_category_frappe to R.string.category_frappe,
    R.drawable.icon_category_latte to R.string.category_latte,
    R.drawable.icon_category_macchiato to R.string.category_macchiato,
    R.drawable.icon_category_mocha to R.string.category_mocha,
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)

private data class Menu(
    val image: Int,
    val title: String,
    val price: String,
)

private val menuData = listOf(
    Menu(R.drawable.menu1, "Pumpkin Spice Latte", "Rp 18.000"),
    Menu(R.drawable.menu2, "Choco Creamy Latte", "Rp 16.000"),
    Menu(R.drawable.menu3, "Tiramisu Coffee Milk", "Rp 28.000"),
    Menu(R.drawable.menu4, "Light Cappuccino", "Rp 20.000"),
)

@Preview(
    showBackground = true, device = Devices.PIXEL_2,
)
@Composable
fun DefaultPreview() {
    KedaiKopiTheme {
        MyApp()
    }
}

@Composable
fun MyApp() {
    KedaiKopiTheme {
        Scaffold(
            bottomBar = { BottomNavigation() }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Banner()
                HomeSection(title = R.string.coffe_category) {
                    FilterRow()
                }
                HomeSection(title = R.string.favorite_menu) {
                    MenuGrid()
                }
            }
        }
    }
}

@Composable
fun Banner(
    modifier: Modifier = Modifier,
) {
    Column {
        Box {
            Image(
                painter = painterResource(R.drawable.banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.height(160.dp)
            )
            SearchBar()
        }

    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }

    TextField(
        value = textState.value,
        onValueChange = { textState.value = it },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(AbsoluteRoundedCornerShape(16.dp))
    )
}

@Composable
fun FilterRow(
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
    ) {
        items(filterData) { item ->
            FilterItem(item.drawable, item.text)
        }
    }
}

@Composable
fun FilterItem(
    drawable: Int,
    text: Int,
    modifier: Modifier = Modifier,

    ) {
    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(text),
            fontSize = 10.sp
        )
    }
}

@Composable
@Preview
fun FilterItem() {
    MaterialTheme {
        FilterItem(R.drawable.icon_category_cappuccino, R.string.category_cappuccino)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuGrid(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(menuData) { item ->
            MenuItem(item.image, item.title, item.price)
        }
    }
}

@Composable
fun MenuItem(
    image: Int,
    title: String,
    price: String,
    modifier: Modifier = Modifier,

    ) {
    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(167.dp)
                .clip(AbsoluteRoundedCornerShape(8.dp))
        )
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.ExtraBold
            )
        )
        Text(
            text = price,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
@Preview
fun MenuItem() {
    MaterialTheme {
        MenuItem(R.drawable.menu1, "Pumpkin Spice Latte", "Rp 18.000")
    }
}

@Composable
fun HomeSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier
                .padding(16.dp)
        )
        content()
    }
}

@Composable
private fun BottomNavigation(modifier: Modifier = Modifier) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.menu_home))
            },
            selected = true,
            onClick = {}
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.menu_cart))
            },
            selected = false,
            onClick = {}
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.menu_history))
            },
            selected = false,
            onClick = {}
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.menu_profile))
            },
            selected = false,
            onClick = {}
        )
    }
}