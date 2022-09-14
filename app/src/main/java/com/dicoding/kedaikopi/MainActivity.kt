package com.dicoding.kedaikopi

import FavoriteScreen
import android.os.Bundle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dicoding.kedaikopi.model.FakeCoffeeDataSource
import com.dicoding.kedaikopi.ui.component.MenuItem
import com.dicoding.kedaikopi.ui.theme.KedaiKopiTheme
import com.dicoding.kedaikopi.ui.theme.LightGray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            FavoriteScreen(onDetailClick = {})
            MyApp()
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite/{coffeeId}")
    object DetailFavorite : Screen("favorite/{coffeeId}") {
        fun createRoute(coffeeId: String) = "favorite/$coffeeId"
    }

    object Profile : Screen("profile")
}

@Composable
fun MainApp() {


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

    val navController = rememberNavController()

    KedaiKopiTheme {
        Scaffold(
            bottomBar = { BottomNavigation(navController) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen()
                }
                composable(Screen.Favorite.route) {
                    FavoriteScreen()
                }
                composable(Screen.Profile.route) {
                    ProfileScreen()
                }
            }

        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
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

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("Profile Screen")
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

data class BottomBarItem(val title: String, val icon: ImageVector, val screen: Screen)

@Composable
private fun BottomNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) {
        val items = listOf(
            BottomBarItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            BottomBarItem(
                title = stringResource(R.string.menu_cart),
                icon = Icons.Default.Favorite,
                screen = Screen.Favorite
            ),
            BottomBarItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile
            ),
        )
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary,
            modifier = modifier
        ) {
            items.map { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(item.title)
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                    unselectedContentColor = LightGray,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}