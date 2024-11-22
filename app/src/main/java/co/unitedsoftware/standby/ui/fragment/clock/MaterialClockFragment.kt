package co.unitedsoftware.standby.ui.fragment.clock

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextClock
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import co.unitedsoftware.standby.R
import co.unitedsoftware.standby.logic.data.WeatherClient
import co.unitedsoftware.standby.logic.isLocationPermissionGranted
import co.unitedsoftware.standby.ui.data.StandbyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MaterialClockFragment : Fragment(), LocationListener {

    companion object {
        const val API_KEY = "bd5e378503939ddaee76f12ad7a97608"
    }

    private var locationManager: LocationManager? = null
    private val standbyViewModel: StandbyViewModel by activityViewModels()

    private lateinit var weatherStatTextView: TextView
    private lateinit var weatherDegreeTextView: TextView

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationManager = ContextCompat.getSystemService(requireContext(), LocationManager::class.java)

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted && requireContext().isLocationPermissionGranted) {
                    requestLocationUpdates()
                }
            }

        if (requireContext().isLocationPermissionGranted) {
            requestLocationUpdates()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }

    private fun requestLocationUpdates() {
        val providers = locationManager!!.getProviders(true)
        for (provider in providers) {
            try {
                locationManager!!.requestLocationUpdates(provider, 5000, 0f, this)
                val location = locationManager!!.getLastKnownLocation(provider)
                if (location != null) {
                    standbyViewModel.setLastKnownLocation(location)
                    fetchWeather(location.latitude, location.longitude)
                    return
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
        Log.e("CompassFragment", "No valid location provider found")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_material_clock, container, false)

        val day = rootView.findViewById<TextClock>(R.id.day)
        val ordinal = rootView.findViewById<TextView>(R.id.ordinal)
        weatherStatTextView = rootView.findViewById(R.id.weather_stat)
        weatherDegreeTextView = rootView.findViewById(R.id.weather_degree)

        day.doOnTextChanged { text, _, _, _ ->
            text?.let { ordinal.text = getOrdinal(it.last()) }
        }
        return rootView
    }

    private fun getOrdinal(lastDigit: Char) = when (lastDigit) {
        '1' -> "st"
        '2' -> "nd"
        '3' -> "rd"
        else -> "th"
    }

    override fun onLocationChanged(p0: Location) {
        Log.d("TAG", "${p0.latitude}, ${p0.longitude}")
        fetchWeather(p0.latitude, p0.longitude)
    }

    private fun fetchWeather(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            try {
                val response = WeatherClient.apiService.getWeather(
                    lat = latitude,
                    lon = longitude,
                    apiKey = API_KEY
                )

                if (response.isSuccessful && response.body() != null) {
                    val weatherResponse = response.body()
                    val firstWeatherItem = weatherResponse?.list?.get(0)

                    if (firstWeatherItem != null) {
                        val temperature = firstWeatherItem.main.temp
                        val description = firstWeatherItem.weather.firstOrNull()?.description

                        withContext(Dispatchers.Main) {
                            description?.let { it1 -> weatherStatTextView.text = it1.replaceFirstChar { if (it. isLowerCase()) it. titlecase(
                                Locale. getDefault()) else it.toString() }.substringBefore(' ') }
                            weatherDegreeTextView.text = String.format(Locale.getDefault(), "%d°", temperature.toInt())
                        }
                    } else {
                        Log.e("WeatherError", "No weather data found.")
                    }
                } else {
                    Log.e("WeatherError", "Error fetching weather: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("WeatherError", "Exception: ${e.message}")
            }
        }
    }

}