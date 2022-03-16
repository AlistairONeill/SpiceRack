package uk.co.alistaironeill.spicerack.spices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uk.co.alistaironeill.spicerack.reusable.AddTextWidget
import uk.co.alistaironeill.spicerack.spice.Spice
import uk.co.alistaironeill.spicerack.spice.SpiceId
import uk.co.alistaironeill.spicerack.spice.SpiceName
import uk.co.alistaironeill.spicerack.spice.SpiceSource

@Composable
fun SpicesScreen(source: SpiceSource) {
    val i = remember { mutableStateOf(0) }
    val refresh: () -> Unit = { i.value += 1 }
    val j = i.value
    val search = remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(Modifier.weight(0.5f)) {
                AddTextWidget("Add Spice") { spiceName ->
                    source.create(SpiceName(spiceName)).orThrow()
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
                .orThrow()
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

@Composable
fun SpiceCard(
    outerRefresh: () -> Unit,
    source: SpiceSource,
    id: SpiceId
) {
    val i = remember { mutableStateOf(0) }
    val innerRefresh: () -> Unit = { i.value += 1 }
    val spice = source.get(id).orThrow()
    val nameToAdd = remember { mutableStateOf("") }
    val j = i.value

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween
            ) {
                Text(spice.id.value)
                Icon(
                    Icons.Default.Delete,
                    "Delete Spice",
                    Modifier.clickable {
                        source.remove(id).orThrow()
                        outerRefresh()
                    }.padding(end = 8.dp)
                )
            }

            Divider(Modifier.height(2.dp).fillMaxWidth())

            Row(Modifier.fillMaxWidth()) {
                Spacer(Modifier.weight(3f))
                Divider(Modifier.width(2.dp))
                Column(Modifier.weight(7f)) {
                    Text(spice.name.value, Modifier.align(Alignment.CenterHorizontally))
                    Divider(Modifier.height(2.dp))
                    spice.aliases.forEach { alias ->
                        Text(alias.value, Modifier.align(Alignment.CenterHorizontally))
                    }
                    AddTextWidget(
                        "Add Alias",
                    ) { alias ->
                        source.addAlias(id, SpiceName(alias)).orThrow()
                        innerRefresh()
                    }
                }
            }
        }
    }
}