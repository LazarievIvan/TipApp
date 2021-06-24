package com.example.tipapp

class Tip() {
    var tipPercent: Double = 0.0;
    fun makeTip(bill: Int, percent: Double): Int{
        var result: Double = bill*percent
        result /= 100
        result = kotlin.math.ceil(result)
        return result.toInt()
    }
}