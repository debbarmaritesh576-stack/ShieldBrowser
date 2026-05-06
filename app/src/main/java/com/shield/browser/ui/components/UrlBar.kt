package com.shield.browser.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.shield.browser.utils.UrlFormatter
import com.shield.browser.data.model.UrlDisplayMode

@Composable
fun UrlBar(
    url: String,
    title: String?,
    isLoading: Boolean,
    displayMode: UrlDisplayMode = UrlDisplayMode.FULL_URL,
    onUrlSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(url, isLoading, displayMode) {
        text = UrlFormatter.getDisplayText(url, title, isLoading, displayMode)
    }

    Card(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Lock, contentDescription = "Secure", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onKeyEvent { 
                        if (it.key == Key.Enter) { 
                            onUrlSubmit(text); focusManager.clearFocus(); keyboardController?.hide(); true 
                        } else false 
                    },
                placeholder = { Text("Search or enter URL", style = MaterialTheme.typography.bodyMedium) },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                ),
                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Search),
                keyboardActions = androidx.compose.ui.text.input.KeyboardActions(onSearch = { 
                    onUrlSubmit(text); focusManager.clearFocus(); keyboardController?.hide() 
                })
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(onClick = { 
                onUrlSubmit(text); focusManager.clearFocus(); keyboardController?.hide() 
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Go")
            }
        }
    }
}