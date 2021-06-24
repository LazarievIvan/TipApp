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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //should implement classes for switches as Switcher class
    private var tip = Tip()
    //private var switchedSwitch: Boolean = false
    lateinit var adView: AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //ads
        MobileAds.initialize(this){}
        adView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        //switches array
        val serviceSwitches: List<Switch?> = getSwitches();
        //set onCLick for switches
        for(switch in serviceSwitches){
            switch?.setOnClickListener { switchListener(serviceSwitches, switch) }
        }

        //initialize EditText
        val billInput: EditText? = findViewById(R.id.finalBill);
        billInput?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                isSwitched(serviceSwitches)
            }
        })
    }

    private fun getSwitches(): List<Switch?> {
        val poorSwitch: Switch? = findViewById(R.id.poorSwitch)
        val okSwitch: Switch? = findViewById(R.id.okSwitch)
        val excelSwitch: Switch? = findViewById(R.id.excelSwitch)

        val switchesList: List<Switch?> = listOf(poorSwitch, okSwitch, excelSwitch)
        return switchesList
    }

    private fun isSwitched(switches: List<Switch?>){
        checkInput();
        for (switch in switches){
            if(switch?.isChecked!!){
                switchListener(switches, switch)
            }
        }
    }

    private fun switchListener(switches: List<Switch?>, clickedSwitch: Switch?) {
        //getting the clicked switch
        val clickedSwitchId = clickedSwitch?.id.toString()
        val finalBill: Int;
        //inputs
        val billInput: EditText? = findViewById(R.id.finalBill)
        if(checkInput()) {
            finalBill = billInput?.text.toString().toInt()
        }else{
            finalBill = 0;
        }
            for (switch in switches) {
                val switchId = switch?.id.toString()
                if (switchId == clickedSwitchId) {
                    switch?.isChecked = true
                    //switchedSwitch = true
                    when (clickedSwitch?.text) {
                        "Poor" -> tip.tipPercent = 10.0
                        "OK" -> tip.tipPercent = 15.0
                        "Excellent" -> tip.tipPercent = 20.0
                    }
                } else if (switchId != clickedSwitchId) {
                    switch?.isChecked = false
                }
            }
            calculate(finalBill);
    }
    private fun calculate(bill: Int){
        val totalTip = tip.makeTip(bill, tip.tipPercent)
        val totalWithTip = bill + totalTip
        val tipPercent = tip.tipPercent.toInt()
        display(totalTip, totalWithTip, tipPercent)
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
    private fun checkInput(): Boolean {
        val input: EditText? = findViewById(R.id.finalBill)
        val inputText = input?.text.toString()
        return when {
            inputText == "" -> {
                //Toast.makeText(this, "Enter the bill!", Toast.LENGTH_SHORT).show()
                false
            }
            inputText.count() > 7 -> {
                Toast.makeText(this, "The number is too large!", Toast.LENGTH_SHORT).show()
                //it works stupid but it works for now
                input?.setText(inputText.dropLast(1));
                false
            }
            else -> {
                true
            }
        }
    }
}
