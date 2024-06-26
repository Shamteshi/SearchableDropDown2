package com.example.searchabledropdown2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.searchabledropdown2.ui.theme.SearchableDropDown2Theme


//update the Ui of the app,
//customize the design to look more appealing

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SearchableDropDown2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchableDropdownPresenter = viewModel()
) {


    val state = viewModel.state



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        TextField(
            value = state.selectedNumber,
            hint = "Select a number",
            items = state.result,
            borderColor = Color.Blue,
            isLoading = state.loadingNumbers,
            onSelect = {
                viewModel.setNumber(it)
            },
            itemAsString = {
                it.toString()
            },
            onLoad = {
                viewModel.loadValues()

            }

        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> TextField(
    modifier: Modifier = Modifier,
    value: T?,
    hint: String,
    items: List<T>,
    isLoading: Boolean = false,
    borderColor: Color = Color.Black,
    borderSize: Dp = 2.dp,
    itemAsString: (T) -> String,
    onLoad: (() -> Unit)? = null,
    onSelect: ((T) -> Unit),
    windowInsets: WindowInsets = WindowInsets.displayCutout
) {

    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    var isVisible by remember {
        mutableStateOf(false)
    }

    var dismiss by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(borderSize, borderColor, RoundedCornerShape(15.dp))
            .clickable(
                interactionSource = null,
                indication = null
            ) {
                if (items.isEmpty()) {
                    onLoad?.invoke()
                } else {
                    isVisible = true
                }


            }
    ) {

        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (value == null) {
                Text(text = hint, color = Color.Gray)
            } else {
                Text(text = itemAsString(value))//whatever the selected value is, pass it to the itamAsString

            }

            if (!isLoading) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Arrow down",
                    tint = Color.Blue
                )
            } else {
                LoadingAnimation()
            }
        }

    }


    if (isVisible && items.isNotEmpty()) {
//        Toast.makeText(context, searchedItems.size.toString(), Toast.LENGTH_LONG).show()

        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = { isVisible = false },
            windowInsets = windowInsets
        ) {


            var searchItem by remember {
                mutableStateOf("")
            }

            var searchedItems by remember {
                mutableStateOf(items)
            }

            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = {
                    Text(text = "Search")
                },
                singleLine = true,
                value = searchItem, onValueChange = {
                    searchItem = it
                    searchedItems = items.filter { item ->
                        if (item is String) {
                            item.lowercase().contains(it.lowercase())
                        } else {
                            itemAsString(item).lowercase().contains(it.lowercase())
                        }
                    }
                })

            Spacer(modifier = modifier.height(12.dp))

            if(searchedItems.isEmpty()){
                Box(modifier = modifier.fillMaxWidth().height(300.dp).padding(16.dp), contentAlignment = Alignment.Center){
                    Text(text = "Whoops found nothing...")
                }
            }

            LazyColumn(modifier = modifier.padding(bottom = bottomPadding)) {
                items(searchedItems.size) { item ->
                    val search = searchedItems.elementAt(item)
                    Box(modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 14.dp)
                        .clickable {
                            isVisible = false
                            onSelect(search)//this passes the item to whatever will use it.
                        }) {
                        Text(text = itemAsString(search))
                    }
                }
            }
        }
    }


}