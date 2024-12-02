package com.example.calculator.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.CalculatorUtils.buttons
import com.example.calculator.CalculatorUtils.calculateFormula
import com.example.calculator.CalculatorUtils.canAddOperator
import com.example.calculator.CalculatorUtils.convertResultToString
import com.example.calculator.CalculatorUtils.operators
import com.example.calculator.CalculatorUtils.shouldAddNewSymbol
import com.example.calculator.CalculatorUtils.updateResultField
import com.example.calculator.ui.theme.bgColor
import com.example.calculator.ui.theme.buttonsColor1
import com.example.calculator.ui.theme.buttonsColor2
import com.example.calculator.ui.theme.buttonsColor3

@Composable
fun App()
{
    var lastSymbol by remember { mutableStateOf("") }
    val symbols = remember { mutableStateListOf<String>() }
    var formulaResult by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Bottom
    ) {

        FormulaDisplay(symbols)
        ResultDisplay(formulaResult)
        Column() {
            for (row in buttons)
            {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (button in row)
                    {
                        Button(
                            onClick = {
                                when (button)
                                {
                                    // deleting from the end of formula
                                    "⌦" ->
                                    {
                                        if (symbols.size > 0)
                                        {
                                            if (lastSymbol in operators) //operator is the last symbol
                                            {
                                                symbols.removeAt(symbols.size - 1)
                                            } else
                                            {
                                                symbols[symbols.size - 1] =
                                                    symbols[symbols.size - 1].removeSuffix(
                                                        lastSymbol
                                                    )

                                                if (symbols.last().isEmpty())
                                                {
                                                    symbols.removeAt(symbols.size - 1)
                                                }
                                            }
                                            lastSymbol =
                                                symbols.lastOrNull()?.lastOrNull()?.toString()
                                                    ?: ""
                                        }
                                    }

                                    // deleting everything
                                    "C" ->
                                    {
                                        symbols.clear()
                                        lastSymbol = ""
                                    }

                                    // calculating the result
                                    "=" ->
                                    {
                                        if (symbols.size > 1)
                                        {
                                            if (lastSymbol in operators || lastSymbol == ",")
                                            {
                                                symbols.removeAt(symbols.size - 1)
                                            }
                                        }

                                        if (symbols.size > 0)
                                        {
                                            val result = calculateFormula(ArrayList(symbols))
                                            if (result != null)
                                            {
                                                symbols.clear()
                                                symbols.add(convertResultToString(result))
                                                lastSymbol =
                                                    symbols.lastOrNull()?.lastOrNull()
                                                        ?.toString()
                                                        ?: ""
                                            }

                                        }

                                    }

                                    "÷", "×", "+", "-", "%" ->
                                    {
                                        if (button == "-" && (lastSymbol in operators || symbols.isEmpty()))
                                        {
                                            symbols.add(button)
                                            lastSymbol = button
                                        }
                                        else if (canAddOperator(lastSymbol, button, symbols))
                                        {
                                            symbols.add(button)
                                            lastSymbol = button
                                        }
                                    }

                                    // clicking digits and comma
                                    else ->
                                    {
                                        if (shouldAddNewSymbol(lastSymbol, symbols))
                                        {
                                            if (button == ",")
                                            {
                                                symbols.add("0" + button)
                                            } else
                                            {
                                                symbols.add(button)
                                            }
                                        }
                                        // If the button is a digit or comma, append it to the last item in the symbols list.
                                        // This way, items in the symbols list can represent integers or doubles, not just single digits.
                                        else if (button != "," || !symbols.last().contains(","))
                                        {
                                            symbols[symbols.size - 1] += button
                                        }

                                        lastSymbol = button
                                    }
                                }

                                // updating result row (smaller one)
                                formulaResult = updateResultField(symbols, lastSymbol)


                            },
                            shape = RectangleShape,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when (button)
                                {
                                    "÷", "×", "-", "+" -> buttonsColor2
                                    "=" -> buttonsColor3
                                    else ->
                                    {
                                        buttonsColor1
                                    }
                                }
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        )
                        {
                            Text(
                                text = button,
                                fontSize = 24.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )

                        }
                    }
                }
            }
        }

    }
}
