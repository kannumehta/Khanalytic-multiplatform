package com.khanalytic.kmm.ui.screens.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.khanalytic.kmm.primaryColor
import com.khanalytic.kmm.primaryContainerColor
import com.khanalytic.kmm.ui.common.DefaultText
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform
import com.khanalytic.kmm.ui.screens.filter.FilterScreen
import kotlinx.coroutines.launch

class UserScreen: Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val model = getScreenModel<UserScreenModel>()
        val selectedScreenType = model.selectedScreenTypeFlow.collectAsStateMultiplatform().value
        val isFilterScreenVisible =
            model.filterScreenVisibleFlow.collectAsStateMultiplatform().value

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    UserScreenType.values().forEach {
                        NavigationDrawerItem(
                            label = { DefaultText(it.screenName) },
                            selected = it == selectedScreenType,
                            icon = { Icon(imageVector = it.icon, contentDescription = null) },
                            shape = RoundedCornerShape(0.dp),
                            onClick = {
                                scope.launch { drawerState.close() }
                                model.onScreenSelected(it)
                            }
                        )
                    }
                }
            },
        ) {
            Navigator(selectedScreenType.screenProducer()) { navigator ->
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Menu,
                                        contentDescription = "Localized description",
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { model.toggleFilterScreenVisibility() }) {
                                    Icon(imageVector = Icons.Filled.Tune, contentDescription = "")
                                }
                            },
                        )
                    },
                ) { contentPadding ->
                    Column(
                        modifier = Modifier.padding(contentPadding).padding(horizontal = 8.dp)
                            .fillMaxSize()
                    ) {
                        CurrentScreen()
                        (navigator.lastItem as FilterScreen)
                            .setFilterScreenVisible(isFilterScreenVisible)
                    }
                }
            }
        }
    }
}