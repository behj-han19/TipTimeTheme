package com.example.tiptime

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    // declares a top-level variable in the class for the binding object
    //lateinit keyword is something new. It's a promise that your code will initialize the variable before using it. If you don't, your app will crash.
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initializes the binding object which you'll use to access Views in the activity_main.xml layout.
        binding = ActivityMainBinding.inflate(layoutInflater)

        //this specifies the root of the hierarchy of views in your app
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener{ calculateTip() }

        // set up a key listener on the text field to listen for when the Enter key is pressed. When that event is detected, hide the keyboard.
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }
    }

    private fun calculateTip(){
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()
        if(cost == null || cost == 0.0){
            displayTip(0.0)
            return
        }

        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId){
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        //use var instead of val because you may need to round up the value if the user selected that option, so the value might change.
        var tip = tipPercentage *  cost
        val roundUp = binding.roundUpSwitch.isChecked
        if(roundUp){
            tip = kotlin.math.ceil(tip)
        }
            // Display the formatted tip value on screen
            displayTip(tip)
        }

    private fun displayTip(tip:Double){
        // USD -> $1234,56  EURO -> €1.234,56 Different region different format
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount,formattedTip)
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

}