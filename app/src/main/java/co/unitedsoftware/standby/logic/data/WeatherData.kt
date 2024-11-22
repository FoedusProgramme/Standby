package co.unitedsoftware.standby.logic.data

data class WeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherItem>
)

data class WeatherItem(
    val dt: Long,
    val main: WeatherMain,
    val weather: List<WeatherCondition>,
    val clouds: WeatherClouds,
    val wind: WeatherWind,
    val visibility: Int,
    val sys: WeatherSys,
    val dt_txt: String
)

data class WeatherMain(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val temp_kf: Double
)

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class WeatherClouds(
    val all: Int
)

data class WeatherWind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class WeatherSys(
    val pod: String
)