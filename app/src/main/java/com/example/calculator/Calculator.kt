package com.example.calculator

object Calculator {

    external fun add(a: Double, b: Double): Double
    external fun subtract(a: Double, b: Double): Double
    external fun multiply(a: Double, b: Double): Double
    external fun divide(a: Double, b: Double): Double
    external fun convertToPercentage(a: Double): Double
    external fun calculatePercentage(a: Double, b: Double): Double

    init {
        System.loadLibrary("calculator")
    }
}