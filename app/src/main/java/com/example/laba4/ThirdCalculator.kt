package com.example.laba4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow
import kotlin.math.sqrt

class ThirdCalculator : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.third_calculator)

        val goBack: Button = findViewById(R.id.back_button)
        goBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val ukMax: EditText = findViewById(R.id.uk_max)
        val uvN: EditText = findViewById(R.id.uv_n)
        val unN: EditText = findViewById(R.id.un_n)
        val smomT: EditText = findViewById(R.id.smom_t)
        val rcN: EditText = findViewById(R.id.rc_n)
        val rcMin: EditText = findViewById(R.id.rc_min)
        val xcN: EditText = findViewById(R.id.xc_n)
        val xcMin: EditText = findViewById(R.id.xc_min)
        val lI: EditText = findViewById(R.id.l_i)
        val rO: EditText = findViewById(R.id.r_o)
        val xO: EditText = findViewById(R.id.x_o)
        val calculate: Button = findViewById(R.id.submit_button)
        val result: TextView = findViewById(R.id.result)

        calculate.setOnClickListener {
            val ukMaxValue = ukMax.text.toString()
            val uvNValue = uvN.text.toString()
            val unNValue = unN.text.toString()
            val smomTValue = smomT.text.toString()
            val rcNValue = rcN.text.toString()
            val rcMinValue = rcMin.text.toString()
            val xcNValue = xcN.text.toString()
            val xcMinValue = xcMin.text.toString()
            val lIValue = lI.text.toString()
            val rOValue = rO.text.toString()
            val xOValue = xO.text.toString()
            var errorMessage = ""

            fun checkAndConvert(value: String, fieldName: String): Double? {
                return if (value.isEmpty()) {
                    errorMessage += "$fieldName is empty.\n "
                    null
                } else {
                    try {
                        value.toDouble()
                    } catch (e: NumberFormatException) {
                        errorMessage += "$fieldName is not a valid number.\n "
                        null
                    }
                }
            }

            val ukMaxVal = checkAndConvert(ukMaxValue, "uk_max")
            val uvNVal = checkAndConvert(uvNValue, "uv_n")
            val unNVal = checkAndConvert(unNValue, "un_n")
            val smomTVal = checkAndConvert(smomTValue, "smom_t")
            val rcNVal = checkAndConvert(rcNValue, "rc_n")
            val rcMinVal = checkAndConvert(rcMinValue, "rc_min")
            val xcNVal = checkAndConvert(xcNValue, "xc_n")
            val xcMinVal = checkAndConvert(xcMinValue, "xc_min")
            val rOVal = checkAndConvert(rOValue, "r_o")
            val lIVal = checkAndConvert(lIValue, "l_i")
            val xOVal = checkAndConvert(xOValue, "x_o")

            if (errorMessage.isNotEmpty()) {
                result.text = errorMessage
            } else {
                val xt = (ukMaxVal!! * (uvNVal!! * uvNVal)) / (100 * smomTVal!!)
                val rsh = rcNVal!!
                val xsh = xcNVal!! + xt
                val zsh = sqrt(rsh * rsh + xsh * xsh)
                val rshMin = rcMinVal!!
                val xshMin = xcMinVal!! + xt
                val zshMin = sqrt(rshMin * rshMin + xshMin * xshMin)
                val ish3 = uvNVal * 1000 / sqrt(3.0) / zsh
                val ish2 = ish3 * sqrt(3.0) / 2
                val ish3Min = uvNVal * 1000 / sqrt(3.0) / zshMin
                val ish2Min = ish3Min * sqrt(3.0) / 2
                val kpr = (unNVal!! * unNVal) / (uvNVal * uvNVal)
                val rshN = kpr * rsh
                val xshN = kpr * xsh
                val zshN = sqrt(rshN * rshN + xshN * xshN)
                val rshNMin = kpr * rshMin
                val xshNMin = kpr * xshMin
                val zshNMin = sqrt(rshNMin * rshNMin + xshNMin * xshNMin)
                val ish3Phase = unNVal * 1000 / sqrt(3.0) / zshN
                val ish2Phase = ish3Phase * sqrt(3.0) / 2
                val ish3PhaseMin = unNVal * 1000 / sqrt(3.0) / zshNMin
                val ish2PhaseMin = ish3PhaseMin * sqrt(3.0) / 2
                val rl = lIVal!! * rOVal!!
                val xl = lIVal * xOVal!!
                val rSumN = rl + rshN
                val xSumN = xl + xshN
                val zSumN = sqrt(rSumN * rSumN + xSumN * xSumN)
                val rSumNMin = rl + rshNMin
                val xSumNMin = xl + xshNMin
                val zSumNMin = sqrt(rSumNMin * rSumNMin + xSumNMin * xSumNMin)
                val isH3 = unNVal * 1000 / sqrt(3.0) / zSumN
                val isH2 = isH3 * sqrt(3.0) / 2
                val isH3Min = unNVal * 1000 / sqrt(3.0) / zSumNMin
                val isH2Min = isH3Min * sqrt(3.0) / 2

                result.text = """
                        I3ш: ${"%.2f".format(ish3)}
                        I2ш: ${"%.2f".format(ish2)}
                        I3ш_min: ${"%.2f".format(ish3Min)}
                        I2ш_min:${"%.2f".format(ish2Min)}
                        I3ш: ${"%.2f".format(ish3Phase)}
                        I2ш: ${"%.2f".format(ish2Phase)}
                        I3ш_min: ${"%.2f".format(ish3PhaseMin)}
                        I2ш_min:${"%.2f".format(ish2PhaseMin)}
                        I3ш: ${"%.2f".format(isH3)}
                        I2ш: ${"%.2f".format(isH2)}
                        I3ш_min: ${"%.2f".format(isH3Min)}
                        I2ш_min:${"%.2f".format(isH2Min)}
                    """.trimIndent()
            }
        }
    }
}
