import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnalyticsViewModel : ViewModel() {
    private val dailyData = listOf(
        1.2f, 1.5f, 1.8f, 2.1f, 1.9f, 1.4f, 1.1f, 1.0f,
        1.3f, 1.7f, 2.3f, 2.5f, 2.0f, 1.6f, 1.2f, 1.0f,
        1.1f, 1.3f, 1.5f, 1.8f, 2.0f, 1.7f, 1.4f, 1.3f
    )

    private val weeklyData = listOf(
        12.5f, 15.3f, 13.8f, 14.2f, 16.0f, 18.4f, 20.1f
    )

    private val _barChartData = MutableLiveData<List<Float>>()
    val barChartData: LiveData<List<Float>> = _barChartData

    init {
        // Set default data to daily
        _barChartData.value = dailyData
    }

    fun updateData(isDaily: Boolean) {
        _barChartData.value = if (isDaily) dailyData else weeklyData
    }
}
