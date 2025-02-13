package com.example.holiday.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.holiday.HolidayViewModel
import com.example.holiday.R
import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: HolidayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.errorMessage.observe(this) { errorMsg ->
            findViewById<TextView>(R.id.txtError).text = errorMsg ?: ""
        }
        viewModel.holidayState.observe(this) { result ->
            val resultText: String = when (result) {
                is HolidayResult.Success -> {
                    if (result.isHoliday) {
                        "🎉 It's a holiday!"
                    } else {
                        "❌ Not a holiday."
                    }
                }

                is HolidayResult.Failure -> {
                    "⚠️ ${result.error.message}"
                }

                HolidayResult.Loading -> {
                    "🚨Checking holiday...🚨"
                }

                null -> ""
            }
            findViewById<TextView>(R.id.txtResult).text = resultText
        }

        findViewById<Button>(R.id.btnCheck).setOnClickListener {
            val year = findViewById<EditText>(R.id.edtYear).text.toString().toIntOrNull() ?: 0
            val month = findViewById<EditText>(R.id.edtMonth).text.toString().toIntOrNull() ?: 0
            val day = findViewById<EditText>(R.id.edtDay).text.toString().toIntOrNull() ?: 0
            viewModel.checkHoliday(year, month, day, HolidayCheckMode.ANY)
        }
    }
}