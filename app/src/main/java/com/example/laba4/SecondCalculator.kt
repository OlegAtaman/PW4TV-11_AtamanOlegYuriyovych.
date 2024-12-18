package com.example.laba4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.sqrt

class SecondCalculator : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.second_calculator)

        val goBack: Button = findViewById(R.id.back_button)
        goBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val ucnInput: EditText = findViewById(R.id.ucn)
        val skInput: EditText = findViewById(R.id.sk)
        val ukPerInput: EditText = findViewById(R.id.uk_per)
        val sNomInput: EditText = findViewById(R.id.s_mom)
        val calculateButton: Button = findViewById(R.id.submit_button)
        val errorTextView: TextView = findViewById(R.id.error1)
        val result1: TextView = findViewById(R.id.result_1)
        val result2: TextView = findViewById(R.id.result_2)
        val result3: TextView = findViewById(R.id.result_3)
        val result4: TextView = findViewById(R.id.result_4)

        calculateButton.setOnClickListener {
            errorTextView.text = ""

            val ucn = ucnInput.text.toString()
            val sk = skInput.text.toString()
            val ukPer = ukPerInput.text.toString()
            val sNom = sNomInput.text.toString()

            val mathContext = MathContext.DECIMAL64
            val errors = mutableListOf<String>()

            val ucnVal = parseBigDecimal(ucn, "UCN", errors)
            val skVal = parseBigDecimal(sk, "SK", errors)
            val ukPerVal = parseBigDecimal(ukPer, "UK_PER", errors)
            val sNomVal = parseBigDecimal(sNom, "S_NOM", errors)

            if (errors.isNotEmpty()) {
                errorTextView.text = errors.joinToString("\n")
            } else {
                val xc = ucnVal!!.pow(2, mathContext)
                    .divide(skVal!!, mathContext)
                val xt = ukPerVal!!
                    .divide(BigDecimal(100), mathContext)
                    .multiply(ucnVal.pow(2, mathContext).divide(sNomVal!!, mathContext), mathContext)
                val xe = xc.add(xt, mathContext)
                val ip = ucnVal.divide(
                    BigDecimal(sqrt(3.0)).multiply(xe, mathContext),
                    mathContext
                )

                result1.text = xc.formatToTwoDecimalPlaces()
                result2.text = xt.formatToTwoDecimalPlaces()
                result3.text = xe.formatToTwoDecimalPlaces()
                result4.text = ip.formatToTwoDecimalPlaces()
            }
        }
    }

    private fun parseBigDecimal(value: String, fieldName: String, errors: MutableList<String>): BigDecimal? {
        return try {
            BigDecimal(value)
        } catch (e: NumberFormatException) {
            errors.add("$fieldName is invalid or empty.")
            null
        }
    }

    private fun BigDecimal.formatToTwoDecimalPlaces(): String {
        return this.setScale(2, java.math.RoundingMode.HALF_UP).toString()
    }
}
