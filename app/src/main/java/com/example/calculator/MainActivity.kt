package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : ComponentActivity()
{

    private val bgColor: Color = Color(0xffdcc9b6)
    private val buttonsColor1: Color = Color(0xffa98467)    // digits
    private val buttonsColor2: Color = Color(0xff6c584c)    // operators
    private val buttonsColor3: Color = Color(0xff606c38)    // equals

    private val operators = listOf("÷", "×", "+", "-")
    private val buttons = listOf(
        listOf("C", "%", "⌦", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("00", "0", ",", "="),
    )

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // CalculatorTheme {
            App();
            //}
        }
    }

    @Preview
    @Composable
    fun App()
    {
        var lastSymbol by remember { mutableStateOf("") }
        val symbols = remember { mutableStateListOf<String>() }
        var formulaResult by remember { mutableDoubleStateOf(0.0) }

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
                    ){
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
                                                    symbols[symbols.size - 1] = symbols[symbols.size - 1].removeSuffix(lastSymbol)

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

                                                val result = calculateFormula(ArrayList(symbols))
                                                symbols.clear()
                                                symbols.add(convertResultToString(result))
                                                lastSymbol =
                                                    symbols.lastOrNull()?.lastOrNull()
                                                        ?.toString()
                                                        ?: ""

                                            }
                                        }

                                        "÷", "×", "+", "-", "%" ->
                                        {
                                            if (canAddOperator(lastSymbol, button, symbols))
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
                                    if (lastSymbol != "")
                                    {
                                        var updatedSymbols = emptyList<String>()

                                        // if at the end is operator - delete it
                                        if (lastSymbol in operators)
                                        {
                                            updatedSymbols = symbols.dropLast(1)
                                        }
                                        // if at the end is comma - delete ony comma
                                        else if (lastSymbol == ",")
                                        {
                                            updatedSymbols =
                                                symbols.dropLast(1) + symbols.last().dropLast(1)
                                        }
                                        // if everything is correct
                                        else if (isLastSymbolDigit(lastSymbol) || lastSymbol == "%")
                                        {
                                            updatedSymbols = symbols
                                        }

                                        if (updatedSymbols.isNotEmpty() && updatedSymbols.size == 1)
                                        {
                                            formulaResult = updatedSymbols.first().replace(",", ".")
                                                .toDoubleOrNull() ?: 0.0
                                        } else
                                        {
                                            formulaResult =
                                                calculateFormula(ArrayList(updatedSymbols))
                                        }
                                    }
                                    else
                                    {
                                        formulaResult = 0.0
                                    }

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


    private @Composable
    fun ResultDisplay(formulaResult: Double)
    {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = convertResultToString(formulaResult),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                color = Color.Gray,
                fontSize = 28.sp,
                textAlign = TextAlign.End,
            )
        }
    }

    private @Composable
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
                fontSize = 48.sp,
                textAlign = TextAlign.End,
            )
        }
    }
    private fun isLastSymbolDigit(symbol: String): Boolean
    {
        return symbol.toCharArray().getOrNull(0)?.isDigit() == true
    }

    private fun canAddOperator(lastSymbol: String, button: String, symbols: List<String>): Boolean
    {
        return (lastSymbol !in operators && symbols.isNotEmpty()) || (symbols.isEmpty() && button == "-")
    }

    private fun shouldAddNewSymbol(lastSymbol: String, symbols: List<String>): Boolean
    {
        return lastSymbol in operators || lastSymbol == "%" || symbols.isEmpty()
    }

    private fun convertResultToString(result: Double): String
    {
        return BigDecimal(result).setScale(8, RoundingMode.HALF_UP).stripTrailingZeros()
            .toPlainString().replace(".", ",")
    }

    private fun calculateFormula(formula: ArrayList<String>): Double
    {
        var i = 1;
        var symbol: String
        var a: Double
        var b: Double

        // handling %
        while (i < formula.size)
        {
            symbol = formula[i];

            // merging expressions with % into one item of formula list
            when (symbol)
            {
                "%" ->
                {
                    a = (formula[i - 1].replace(",", ".")).toDoubleOrNull() ?: 0.0

                    if (i == formula.size - 1 || formula[i + 1] in operators)
                    {
                        formula[i - 1] = Calculator.convertToPercentage(a).toString()

                    } else if (i + 1 < formula.size)
                    {
                        b = (formula[i + 1].replace(",", ".")).toDouble()

                        formula[i - 1] = Calculator.calculatePercentage(a, b).toString()

                        formula.removeAt(i + 1)
                    }

                    formula.removeAt(i)
                }

                else -> i++
            }
        }

        i = 1


        // handling division and multiplication
        while (i < formula.size)
        {
            symbol = formula[i];

            when (symbol)
            {
                "÷" ->
                {
                    a = (formula[i - 1].replace(",", ".")).toDoubleOrNull() ?: 0.0
                    b = (formula[i + 1].replace(",", ".")).toDoubleOrNull() ?: 0.0

                    formula[i - 1] = Calculator.divide(a, b).toString()
                    formula.removeAt(i + 1)
                    formula.removeAt(i)
                }

                "×" ->
                {
                    a = (formula[i - 1]).toDoubleOrNull() ?: 0.0
                    b = (formula[i + 1]).toDoubleOrNull() ?: 0.0

                    formula[i - 1] = Calculator.multiply(a, b).toString()
                    formula.removeAt(i + 1)
                    formula.removeAt(i)
                }

                else -> i++
            }
        }

        i = 1

        //handling addition and subtraction
        while (i < formula.size)
        {
            symbol = formula[i];

            when (symbol)
            {
                "+" ->
                {
                    a = formula[i - 1].replace(",", ".").toDoubleOrNull() ?: 0.0
                    b = formula[i + 1].replace(",", ".").toDoubleOrNull() ?: 0.0

                    formula[i - 1] = Calculator.add(a, b).toString()
                    formula.removeAt(i + 1)
                    formula.removeAt(i)
                }

                "-" ->
                {
                    a = formula[i - 1].replace(",", ".").toDoubleOrNull() ?: 0.0
                    b = formula[i + 1].replace(",", ".").toDoubleOrNull() ?: 0.0

                    formula[i - 1] = Calculator.subtract(a, b).toString()
                    formula.removeAt(i + 1)
                    formula.removeAt(i)
                }

                else -> i++

            }
        }

        return formula[0].toDoubleOrNull() ?: 0.0
    }
}

