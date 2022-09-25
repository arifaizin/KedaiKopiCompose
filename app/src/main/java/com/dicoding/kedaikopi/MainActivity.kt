package com.dicoding.kedaikopi

import FavoriteScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.dicoding.kedaikopi.ui.screen.cart.CartScreen
import com.dicoding.kedaikopi.ui.screen.detail.DetailScreen
import com.dicoding.kedaikopi.ui.screen.home.HomeScreen
import com.dicoding.kedaikopi.ui.theme.KedaiKopiTheme
import com.dicoding.kedaikopi.ui.theme.LightGray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object DetailFavorite : Screen("favorite/{coffeeId}") {
        fun createRoute(coffeeId: Long) = "favorite/$coffeeId"
    }
    object Cart : Screen("cart")
}

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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    KedaiKopiTheme {
        Scaffold(
            bottomBar = {
                if (currentDestination != Screen.DetailFavorite.route) {
                    BottomNavigation(navController)
                }
            }
        ) { innerPadding ->
            val uri = "https://www.dicoding.com"

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen()
                }
                composable(Screen.Favorite.route) {
                    FavoriteScreen(
                        navigateToDetail = {
                            navController.navigate(Screen.DetailFavorite.createRoute(it))
                        }
                    )
                }
                composable(
                    route = Screen.DetailFavorite.route,
                    arguments = listOf(navArgument("coffeeId") { type = NavType.LongType }),
                    deepLinks = listOf(navDeepLink { uriPattern = "$uri/detail/{coffeeId}" })
                ) {
                    val id = it.arguments?.getLong("coffeeId") ?: -1L
                    DetailScreen(id,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        navigateToCart = {
                            navController.navigate(Screen.Cart.route){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        })
                }
                composable(Screen.Cart.route) {
                    CartScreen(navigateToSuccess = {})
                }
            }

        }
    }
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("Profile Screen")
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
                title = stringResource(R.string.menu_favorite),
                icon = Icons.Default.Favorite,
                screen = Screen.Favorite
            ),
            BottomBarItem(
                title = stringResource(R.string.menu_cart),
                icon = Icons.Default.ShoppingCart,
                screen = Screen.Cart
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