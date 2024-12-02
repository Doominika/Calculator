package com.example.calculator

import java.math.BigDecimal
import java.math.RoundingMode

object CalculatorUtils
{
    val operators = listOf("÷", "×", "+", "-")
    val buttons = listOf(
        listOf("C", "%", "⌦", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("00", "0", ",", "="),
    )

    fun isSymbolDigit(symbol: String): Boolean
    {
        return symbol.toCharArray().getOrNull(0)?.isDigit() == true
    }

    fun canAddOperator(lastSymbol: String, button: String, symbols: List<String>): Boolean
    {
        return (lastSymbol !in operators && symbols.isNotEmpty()) || (symbols.isEmpty() && button == "-")
    }

    fun shouldAddNewSymbol(lastSymbol: String, symbols: List<String>): Boolean
    {
        return lastSymbol in operators || lastSymbol == "%" || symbols.isEmpty()
    }

    fun convertResultToString(result: Double): String
    {
        return BigDecimal(result).setScale(8, RoundingMode.HALF_UP).stripTrailingZeros()
            .toPlainString().replace(".", ",")
    }

    fun updateResultField(symbols: List<String>, lastSymbol: String) : String
    {
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
            else if (isSymbolDigit(lastSymbol) || lastSymbol == "%")
            {
                updatedSymbols = symbols
            }


            // there is no formula yet
            if (updatedSymbols.isNotEmpty() && updatedSymbols.size == 1)
            {
                return convertResultToString(updatedSymbols.first().replace(",", ".")
                    .toDoubleOrNull() ?: 0.0)
            } else if(updatedSymbols.size > 1)
            {
                val result: Double? = calculateFormula(ArrayList(updatedSymbols))
                if(result == null)
                {
                    return "Nie można dzielić przez 0"
                }
                return convertResultToString(result)
            }
            else
            {
                return ""
            }
        } else
        {
            return "0"
        }
    }

    fun calculateFormula(formula: ArrayList<String>): Double?
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
                    //Toast.makeText(this, formula[i - 1], Toast.LENGTH_SHORT).show()

                    formula.removeAt(i)
                }

                else -> i++
            }
        }


        i=0

        while (i < formula.size)
        {
            symbol = formula[i];

            // merging expressions with minus into one item of formula list
            when (symbol)
            {
                // checking if there are negative values
                "-" ->
                {
                    // minus at the beginning of formula or before number
                    if ((i == 0 || formula[i - 1] in operators) && i + 1 < formula.size)
                    {
                        a = (formula[i + 1].replace(",", ".")).toDoubleOrNull() ?: 0.0
                        formula[i] = Calculator.multiply(a, (-1.0)).toString()
                        formula.removeAt(i + 1)

                    }

                    i++
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

                    if(b == 0.0) return null

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