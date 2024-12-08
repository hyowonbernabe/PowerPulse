package com.example.powerpulse.ui.analytics

import AnalyticsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.powerpulse.R
import com.example.powerpulse.databinding.FragmentAnalyticsBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.ValueFormatter

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private val analyticsViewModel: AnalyticsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val barChart = binding.barChart

        // Set up BarChart appearance as before
        barChart.setDrawGridBackground(false)
        barChart.description.isEnabled = true

        // Observe the bar chart data
        analyticsViewModel.barChartData.observe(viewLifecycleOwner) { data ->
            val entries = data.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
            val dataSet = BarDataSet(entries, "Power Consumption")
            dataSet.color = resources.getColor(R.color.brand_primary)
            val barData = BarData(dataSet)
            barChart.data = barData

            // Check if it's daily or weekly data and update the X-axis accordingly
            if (data.size > 7) { // Assuming >7 entries indicate hourly data
                barChart.description.text = "Power Consumption Daily"
                barChart.xAxis.valueFormatter = HourXAxisValueFormatter()
                barChart.xAxis.labelCount = 4
            } else {
                barChart.description.text = "Power Consumption Weekly"
                barChart.xAxis.valueFormatter = WeekXAxisValueFormatter()
                barChart.xAxis.labelCount = 7
            }

            barChart.invalidate() // Refresh the chart to apply changes
        }


        // Button click listeners
        binding.totalPowerDaily.setOnClickListener {
            analyticsViewModel.updateData(isDaily = true)
        }

        binding.TotalPowerWeekly.setOnClickListener {
            analyticsViewModel.updateData(isDaily = false)
        }

        return root
    }

    class WeekXAxisValueFormatter : ValueFormatter() {
        private val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        override fun getFormattedValue(value: Float): String {
            val index = value.toInt()
            return if (index in days.indices) days[index] else ""
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class HourXAxisValueFormatter : ValueFormatter() {
    // This formatter formats the X-axis labels as hours (0-23)
    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()}h"
    }
}