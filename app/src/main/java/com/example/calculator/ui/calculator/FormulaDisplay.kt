package com.example.calculator.ui.calculator

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.mainTextColor

@Composable
fun FormulaDisplay(symbols: List<String>)
{
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = symbols.joinToString(separator = ""),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            color = mainTextColor,
            fontSize = 48.sp,
            textAlign = TextAlign.End,
        )
    }
}