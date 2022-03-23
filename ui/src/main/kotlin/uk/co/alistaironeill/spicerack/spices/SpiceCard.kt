@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.spices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Light
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextAlign.Companion.Left
import androidx.compose.ui.text.style.TextAlign.Companion.Right
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.co.alistaironeill.spicerack.error.orAlert
import uk.co.alistaironeill.spicerack.reusable.AddTextWidget
import uk.co.alistaironeill.spicerack.reusable.RGBWidget
import uk.co.alistaironeill.spicerack.spice.RGB
import uk.co.alistaironeill.spicerack.spice.SpiceId
import uk.co.alistaironeill.spicerack.spice.SpiceName
import uk.co.alistaironeill.spicerack.spice.SpiceSource

@Composable
internal fun SpiceCard(
    outerRefresh: () -> Unit,
    source: SpiceSource,
    id: SpiceId
) {
    val i = remember { mutableStateOf(0) }
    val innerRefresh: () -> Unit = { i.value += 1 }
    val spice = source.get(id).orAlert { null }
    val j = i.value

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        elevation = 10.dp
    ) {
        if (spice != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TopBar(spice.name) {
                    source.delete(id).orAlert { }
                    outerRefresh()
                }

                Spacer(Modifier.height(8.dp))
                Divider(Modifier.height(2.dp).fillMaxWidth())
                Spacer(Modifier.height(8.dp))

                Row(Modifier.fillMaxWidth()) {
                    LeftPanel(
                        Modifier.weight(1f),
                        spice.colour
                    ) { colour ->
                        source.setColour(id, colour).orAlert { }
                        innerRefresh()
                    }

                    Divider(Modifier.width(2.dp))
                    AliasPanel(
                        Modifier.weight(1f),
                        spice.aliases,
                        { alias ->
                            source.addAlias(id, alias).orAlert { }
                            innerRefresh()
                        },
                        { alias ->
                            source.removeAlias(id, alias).orAlert { }
                            innerRefresh()
                        }
                    )
                }

                Text(
                    id.value,
                    Modifier.fillMaxWidth(),
                    textAlign = Right,
                    fontWeight = Light
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    name: SpiceName,
    delete: () -> Unit
) {
    val deleteClicked = remember { mutableStateOf(false) }

    Row(
        Modifier.fillMaxWidth(),
        SpaceBetween,
        CenterVertically
    ) {
        Spacer(Modifier.weight(1f))

        Text(
            name.value,
            Modifier.weight(2f),
            fontSize = 24.sp,
            fontWeight = Bold,
            textAlign = Center
        )

        Row(
            Modifier.weight(1f),
            horizontalArrangement = End
        ) {
            if (deleteClicked.value) {
                Text("Are you sure?")
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.Default.Check,
                    "Yes",
                    Modifier.clickable {
                        delete()
                    }
                )
                Icon(
                    Icons.Default.Close,
                    "No",
                    Modifier.clickable {
                        deleteClicked.value = false
                    }
                )
            } else {
                Icon(
                    Icons.Default.Delete,
                    "Delete Spice",
                    Modifier.clickable {
                        deleteClicked.value = true
                    }.padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun LeftPanel(
    modifier: Modifier,
    colour: RGB,
    setColour: (RGB) -> Unit
) {
    Column(
        modifier,
        SpaceBetween,
        Alignment.CenterHorizontally
    ) {
        RGBWidget(
            colour,
            setColour
        )
    }
}

@Composable
fun AliasPanel(
    modifier: Modifier,
    aliases: Set<SpiceName>,
    add: (SpiceName) -> Unit,
    remove: (SpiceName) -> Unit
) {
    Column(modifier) {
        Text("Also Known As:")

        Spacer(Modifier.height(16.dp))

        aliases.forEach { alias ->
            AliasPanel(alias) { remove(alias) }
        }

        Spacer(Modifier.height(16.dp))

        AddTextWidget(
            "Add Alias",
        ) { alias ->
            add(SpiceName(alias))
        }
    }
}

@Composable
fun AliasPanel(
    alias: SpiceName,
    remove: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically
    ) {
        Spacer(Modifier.weight(1f))

        Icon(
            Icons.Default.Close,
            "Remove",
            Modifier.weight(1f)
                .clickable(onClick = remove)
        )

        Text(
            alias.value,
            Modifier.weight(6f),
            textAlign = Left
        )

        Spacer(Modifier.weight(1f))
    }

}