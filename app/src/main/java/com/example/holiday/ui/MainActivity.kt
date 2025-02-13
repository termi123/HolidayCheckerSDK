package com.example.holiday.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.holiday.HolidayViewModel
import com.example.holiday.R
import com.example.holiday.databinding.ActivityMainBinding
import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: HolidayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.holidayState.observe(this) { result ->
            binding.btnCheck.isEnabled = result != HolidayResult.Loading
            val resultText: String
            when (result) {
                is HolidayResult.Success -> {
                    resultText = if (result.isHoliday) {
                        "🎉 It's a holiday!"
                    } else {
                        "❌ Not a holiday."
                    }
                }

                is HolidayResult.Failure -> {
                    resultText = "⚠️ ${result.error.message}"
                }

                HolidayResult.Loading -> {
                    resultText = "🚨Checking holiday...🚨"
                }
            }
            binding.txtResult.text = resultText
        }

        binding.btnCheck.setOnClickListener {
            val year = binding.edtYear.text.toString().toIntOrNull() ?: 0
            val month = binding.edtMonth.text.toString().toIntOrNull() ?: 0
            val day = binding.edtDay.text.toString().toIntOrNull() ?: 0
            viewModel.checkHoliday(year, month, day, HolidayCheckMode.ANY)
        }
    }
}