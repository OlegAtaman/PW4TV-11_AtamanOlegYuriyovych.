package com.example.laba4

import android.util.Log
import java.math.BigDecimal
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class FirstCalculator : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.first_calculator)

        val spinnerPrimary = findViewById<Spinner>(R.id.selection_1)
        val spinnerSecondary = findViewById<Spinner>(R.id.selection_2)
        val backButton = findViewById<Button>(R.id.back_button)

        initializeSpinner(spinnerPrimary, R.array.spinner_options_1)
        initializeSpinner(spinnerSecondary, R.array.spinner_options_2)

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val inputFields = InputFields(
            findViewById(R.id.unom),
            findViewById(R.id.ik),
            findViewById(R.id.tf),
            findViewById(R.id.power_tp),
            findViewById(R.id.sm),
            findViewById(R.id.tm),
            findViewById(R.id.ct)
        )

        val calculateButtonPrimary = findViewById<Button>(R.id.submit_button)
        val calculateButtonSecondary = findViewById<Button>(R.id.second_submit_button)
        val errorMessageDisplay = ErrorDisplays(
            findViewById(R.id.error1), findViewById(R.id.error2)
        )
        val resultDisplays = ResultDisplays(
            findViewById(R.id.result_1), findViewById(R.id.result_2),
            findViewById(R.id.result_3), findViewById(R.id.second_result)
        )

        val computationData = mapOf(
            "Неізольовані проводи та шини-Мідні" to listOf(
                1000.0..3000.0 to 2.5,
                3001.0..5000.0 to 2.1,
                5001.0..Double.MAX_VALUE to 1.8
            ),
            "Неізольовані проводи та шини-Алюмінієві" to listOf(
                1000.0..3000.0 to 1.3,
                3001.0..5000.0 to 1.1,
                5001.0..Double.MAX_VALUE to 1.0
            ),
            "Кабелі з паперовою і проводи з гумовою та полівініхлоридною ізоляцією з жилами-Мідні" to listOf(
                1000.0..3000.0 to 3.0,
                3001.0..5000.0 to 2.5,
                5001.0..Double.MAX_VALUE to 2.0
            ),
            "Кабелі з паперовою і проводи з гумовою та полівініхлоридною ізоляцією з жилами-Алюмінієві" to listOf(
                1000.0..3000.0 to 1.6,
                3001.0..5000.0 to 1.4,
                5001.0..Double.MAX_VALUE to 1.2
            ),
            "Кабелі з гумовою та пластиковою ізоляцією з жилами-Мідні" to listOf(
                1000.0..3000.0 to 3.5,
                3001.0..5000.0 to 3.1,
                5001.0..Double.MAX_VALUE to 2.7
            ),
            "Кабелі з гумовою та пластиковою ізоляцією з жилами-Алюмінієві" to listOf(
                1000.0..3000.0 to 1.9,
                3001.0..5000.0 to 1.7,
                5001.0..Double.MAX_VALUE to 1.6
            )
        )

        calculateButtonPrimary.setOnClickListener {
            handlePrimaryCalculation(spinnerPrimary, spinnerSecondary, inputFields, resultDisplays, errorMessageDisplay, computationData)
        }

        calculateButtonSecondary.setOnClickListener {
            handleSecondaryCalculation(inputFields, resultDisplays, errorMessageDisplay)
        }
    }

    private fun initializeSpinner(spinner: Spinner, arrayResource: Int) {
        val adapter = ArrayAdapter.createFromResource(this, arrayResource, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun handlePrimaryCalculation(
        spinner1: Spinner,
        spinner2: Spinner,
        inputs: InputFields,
        results: ResultDisplays,
        errors: ErrorDisplays,
        computationData: Map<String, List<Pair<ClosedRange<Double>, Double>>>
    ) {
        val unomVal = inputs.validateAndGetDouble(inputs.unomInput, "UNOM", errors.errorPrimary)
        val smVal = inputs.validateAndGetDouble(inputs.smInput, "SM", errors.errorPrimary)
        val tmVal = inputs.validateAndGetDouble(inputs.tmInput, "TM", errors.errorPrimary)

        if (errors.hasErrors()) return

        Log.d("Debug", "Spinner1: ${spinner1.selectedItem}, Spinner2: ${spinner2.selectedItem}")

        val selectionKey = "${spinner1.selectedItem}-${spinner2.selectedItem}"

        Log.d("Debug", "Key: ${selectionKey}")
        Log.d("Debug", "Key: Неізольовані проводи та шини-Мідні")

        val rangeMultiplier = computationData[selectionKey]?.firstOrNull { tmVal in it.first }?.second ?: 0.0

        val calculatedIM = ((smVal.toBigDecimal().div(BigDecimal(2.0)))
            .div(BigDecimal(sqrt(3.0)).multiply(unomVal.toBigDecimal())))
            .toDouble().formatTwoDecimals()

        val calculatedIMPA = (calculatedIM.toBigDecimal().multiply(BigDecimal(2.0))).toDouble().formatTwoDecimals()
        val calculatedSEK = (calculatedIM.toBigDecimal().div(rangeMultiplier.toBigDecimal())).toDouble().formatTwoDecimals()

        results.resultPrimary1.text = calculatedIM
        results.resultPrimary2.text = calculatedIMPA
        results.resultPrimary3.text = calculatedSEK
    }

    private fun handleSecondaryCalculation(
        inputs: InputFields,
        results: ResultDisplays,
        errors: ErrorDisplays
    ) {
        val ikVal = inputs.validateAndGetDouble(inputs.ikInput, "IK", errors.errorSecondary)
        val tfVal = inputs.validateAndGetDouble(inputs.tfInput, "TF", errors.errorSecondary)
        val ctVal = inputs.validateAndGetDouble(inputs.ctInput, "CT", errors.errorSecondary)

        if (errors.hasErrors()) return

        val calculatedSMin = ((ikVal.toBigDecimal().multiply(BigDecimal(1000))
            .multiply(BigDecimal(sqrt(tfVal))))
            .div(ctVal.toBigDecimal())).toDouble().formatTwoDecimals()
        results.resultSecondary.text = calculatedSMin
    }

    private fun Double.formatTwoDecimals() = "%.2f".format(this)

    data class InputFields(
        val unomInput: EditText,
        val ikInput: EditText,
        val tfInput: EditText,
        val powerTPInput: EditText,
        val smInput: EditText,
        val tmInput: EditText,
        val ctInput: EditText
    ) {
        fun validateAndGetDouble(input: EditText, fieldName: String, errorView: TextView): Double {
            val value = input.text.toString()
            return try {
                value.toDouble()
            } catch (e: NumberFormatException) {
                errorView.text = "$fieldName is invalid"
                0.0
            }
        }
    }

    data class ErrorDisplays(val errorPrimary: TextView, val errorSecondary: TextView) {
        fun hasErrors() = errorPrimary.text.isNotEmpty() || errorSecondary.text.isNotEmpty()
    }

    data class ResultDisplays(
        val resultPrimary1: TextView,
        val resultPrimary2: TextView,
        val resultPrimary3: TextView,
        val resultSecondary: TextView
    )
}
