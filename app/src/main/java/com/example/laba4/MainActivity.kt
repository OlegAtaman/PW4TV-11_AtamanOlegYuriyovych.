package com.example.laba4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val calc1: Button = findViewById(R.id.first_calc)
        val calc2: Button = findViewById(R.id.second_calc)
        val calc3: Button = findViewById(R.id.third_calc)

        calc1.setOnClickListener {
            val intent = Intent(this, FirstCalculator::class.java)
            startActivity(intent)
        }

        calc2.setOnClickListener {
            val intent = Intent(this, SecondCalculator::class.java)
            startActivity(intent)
        }

        calc3.setOnClickListener {
            val intent = Intent(this, ThirdCalculator::class.java)
            startActivity(intent)
        }
    }
}