package com.example.tipapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    private var tip = Tip()
    private lateinit var adView: AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //ads
        MobileAds.initialize(this){}
        adView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        //initialize EditText
        val billInput: EditText? = findViewById(R.id.finalBill)
        var billAmount = 0
        var rate = 10.0
        billInput?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(checkInput(billInput)){
                    billAmount = s.toString().toInt()
                    tip.tipPercent = rate
                    calculate(billAmount, tip.tipPercent)
                }else{
                    billAmount = 0
                    hideTipInfo()
                }
            }
        })
        //initialize SeekBar
        val rateBar: SeekBar? = findViewById(R.id.ratingSeekBar)
        rateBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(checkInput(billInput)){
                    rate = (progress+10).toDouble()
                    tip.tipPercent = rate
                    calculate(billAmount, tip.tipPercent)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if(checkInput(billInput)) {
                    tip.tipPercent = rate
                    calculate(billAmount, tip.tipPercent)
                }
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
    private fun calculate(bill: Int, tipRate: Double){
        val totalTip = tip.makeTip(bill, tipRate)
        val totalWithTip = bill + totalTip
        display(totalTip, totalWithTip, tipRate.toInt())
    }
    private fun display(totalTip: Int, totalWithTip: Int, tipPercent: Int) {
        //TextViews
        val resultTextTV: TextView? = findViewById(R.id.resultText)
        val tipInfoTV: TextView? = findViewById(R.id.tipInfo)
        val totalBillTV: TextView? = findViewById(R.id.totalBill)
        //Original Text from TextViews above
        var resultText = resources.getString(R.string.resultTextTV)
        var tipInfo = resources.getString(R.string.tipInfoTV)
        var totalBill = resources.getString(R.string.totalBillTV)
        //turning the visibility on
        resultTextTV?.visibility = View.VISIBLE
        tipInfoTV?.visibility = View.VISIBLE
        totalBillTV?.visibility = View.VISIBLE
        //concatenations
        resultText = "$resultText $totalTip"
        tipInfo = "$tipPercent $tipInfo"
        totalBill = "$totalBill $totalWithTip"
        //show
        resultTextTV?.text = resultText
        tipInfoTV?.text = tipInfo
        totalBillTV?.text = totalBill
    }
    private fun hideTipInfo(){
        val resultTextTV: TextView? = findViewById(R.id.resultText)
        val tipInfoTV: TextView? = findViewById(R.id.tipInfo)
        val totalBillTV: TextView? = findViewById(R.id.totalBill)
        resultTextTV?.visibility = View.INVISIBLE
        tipInfoTV?.visibility = View.INVISIBLE
        totalBillTV?.visibility = View.INVISIBLE
    }
    private fun checkInput(input: EditText?): Boolean {
        val inputText = input?.text.toString()
        return when {
            inputText == "" -> {
                //Toast.makeText(this, "Enter the bill!", Toast.LENGTH_SHORT).show()
                false
            }
            inputText.count() > 7 -> {
                Toast.makeText(this, "The number is too large!", Toast.LENGTH_SHORT).show()
                //it works stupid but it works for now
                input?.setText(inputText.dropLast(1))
                false
            }
            else -> {
                true
            }
        }
    }
}
