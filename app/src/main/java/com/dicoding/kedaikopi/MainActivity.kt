package com.dicoding.kedaikopi

import FavoriteScreen
import android.media.Image
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.dicoding.kedaikopi.model.FakeCoffeeDataSource
import com.dicoding.kedaikopi.ui.component.MenuItem
import com.dicoding.kedaikopi.ui.theme.KedaiKopiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FavoriteScreen(navigateToHomeScreen = {})
//            MyApp()
        }
    }
}

data class Category(
    @DrawableRes val imageCategory: Int,
    @StringRes val textCategory: Int
)

private val dummyCategory = listOf(
    R.drawable.icon_category_all to R.string.category_all,
    R.drawable.icon_category_americano to R.string.category_americano,
    R.drawable.icon_category_cappuccino to R.string.category_cappuccino,
    R.drawable.icon_category_espresso to R.string.category_espresso,
    R.drawable.icon_category_frappe to R.string.category_frappe,
    R.drawable.icon_category_latte to R.string.category_latte,
    R.drawable.icon_category_macchiato to R.string.category_macchiato,
    R.drawable.icon_category_mocha to R.string.category_mocha,
).map { Category(it.first, it.second) }

@Preview(showBackground = true, device = Devices.PIXEL_2)
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
                    CategoryRow()
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
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(AbsoluteRoundedCornerShape(16.dp))
    )
}

@Composable
fun CategoryRow(
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
    ) {
        items(dummyCategory) { category ->
            CategoryItem(category)
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    modifier: Modifier = Modifier,

    ) {
    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(category.imageCategory),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            modifier = Modifier.paddingFromBaseline(top = 16.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(category.textCategory),
            fontSize = 8.sp
        )
    }
}

@Composable
@Preview
fun CategoryItem() {
    MaterialTheme {
        CategoryItem(Category(R.drawable.icon_category_cappuccino, R.string.category_cappuccino))
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
        items(FakeCoffeeDataSource.dummyCoffees) { menu ->
            MenuItem(
                image = menu.image,
                title = menu.title,
                price = menu.price,
                modifier = Modifier.fillMaxWidth()
            )
        }
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