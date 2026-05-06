package com.shield.browser.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShieldTopAppBar(
    title: String,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        modifier = modifier
    )
}