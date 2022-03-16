package uk.co.alistaironeill.spicerack.reusable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddTextWidget(
    label: String,
    modifier: Modifier = Modifier,
    onSubmit: (String) -> Unit
) {
    val text = remember { mutableStateOf("") }
    OutlinedTextField(
        value = text.value,
        trailingIcon = {
            Icon(
                Icons.Default.Check,
                contentDescription = "Add",
                modifier = Modifier.clickable {
                    onSubmit(text.value)
                    text.value = ""
                }.padding(end = 8.dp)
            )
        },
        modifier = modifier.padding(8.dp).fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        label = { Text(text = label) },
        onValueChange = {
            text.value = it
        }
    )
}

