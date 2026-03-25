package com.klen0010.flinders.zootreasurehunt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    isSortByName: Boolean,
    onSortChange: (Boolean) -> Unit
) {
    Column{
        Text(
            text = "Settings",
            color = Color.Black,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(top = 30.dp),
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Sort Order",
            color = Color.Black,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 30.dp, top = 30.dp, bottom = 15.dp),
            fontWeight = FontWeight.Bold
        )
        Row{
            RadioButton(
                selected = isSortByName,
                onClick = {onSortChange(true)}
            )
            Text("Sort by name")
        }
        Row{
            RadioButton(
                selected = !isSortByName,
                onClick = {onSortChange(false)}
            )
            Text("Sort by date")
        }
    }
}