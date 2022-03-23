@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.spices

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uk.co.alistaironeill.spicerack.error.orAlert
import uk.co.alistaironeill.spicerack.reusable.AddTextWidget
import uk.co.alistaironeill.spicerack.spice.Spice
import uk.co.alistaironeill.spicerack.spice.SpiceName
import uk.co.alistaironeill.spicerack.spice.SpiceSource

@Composable
fun SpicesScreen(source: SpiceSource) {
    //TODO: Figure out a nicer way of triggering the Compose refreshes
    val i = remember { mutableStateOf(0) }
    val refresh: () -> Unit = { i.value += 1 }
    i.value
    val search = remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(Modifier.weight(0.5f)) {
                AddTextWidget("Add Spice") { spiceName ->
                    source.create(SpiceName(spiceName)).orAlert { }
                    refresh()
                }
            }

            Box(Modifier.weight(0.5f)) {
                OutlinedTextField(
                    value = search.value,
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    modifier = Modifier.padding(8.dp).fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    label = { Text(text = "Search") },
                    onValueChange = {
                        search.value = it
                    }
                )
            }
        }

        LazyColumn(Modifier.fillMaxSize()) {
            val ids = source.get()
                .orAlert { emptySet() }
                .filter(matches(search.value))
                .map(Spice::id)

            items(
                ids.size,
            ) { index ->
                SpiceCard(
                    refresh,
                    source,
                    ids[index]
                )
            }
        }
    }
}

private fun matches(search: String): (Spice) -> Boolean = { spice ->
    spice.name.value.contains(search, true) ||
            spice.aliases.map(SpiceName::value).any { it.contains(search, true) }
}

