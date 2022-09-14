import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.kedaikopi.data.CoffeeRepository
import com.dicoding.kedaikopi.model.FakeCoffeeDataSource
import com.dicoding.kedaikopi.model.FakeCoffeeDataSource.dummyCoffees
import com.dicoding.kedaikopi.model.OrderCoffee
import com.dicoding.kedaikopi.ui.ViewModelFactory
import com.dicoding.kedaikopi.ui.common.UiState
import com.dicoding.kedaikopi.ui.component.MenuItem
import com.dicoding.kedaikopi.ui.screen.favorite.FavoriteViewModel

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = viewModel(factory = ViewModelFactory(CoffeeRepository()))
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.loadCoffeeDrinks()
            }
            is UiState.Success -> {
                FavoriteContent(
                    listCoffee = uiState.data,
                    onDetailClick = {}
                )
            }
            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteContent(
    listCoffee: List<OrderCoffee>,
    onDetailClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(listCoffee) { menu ->
            MenuItem(
                image = menu.coffee.image,
                title = menu.coffee.title,
                price = menu.coffee.price,
                modifier = Modifier.clickable {
                    onDetailClick(menu.coffee.id)
                }
            )
        }
    }
}