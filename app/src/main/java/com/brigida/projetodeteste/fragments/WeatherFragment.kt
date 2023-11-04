package com.brigida.projetodeteste.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.brigida.projetodeteste.BuildConfig
import com.brigida.projetodeteste.data.ApiInterface
import com.brigida.projetodeteste.data.model.CurrentWeather
import com.brigida.projetodeteste.databinding.FragmentWeatherBinding
import com.brigida.projetodeteste.utils.NetworkUtils
import com.squareup.picasso.Picasso
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentWeather("Fortaleza")
        binding.apply {
            btnSearchCity.setOnClickListener {
                val city = etCity.text.toString()
                getCurrentWeather(city)
            }
        }
    }

    private fun getCurrentWeather(city: String) {
        val retrofitClient = NetworkUtils.getRetrofitInstance()
        val endPoint = retrofitClient.create(ApiInterface::class.java)
        endPoint
            .getCurrentWeather(city, BuildConfig.OPEN_WEATHER_API_KEY, "metric", "pt_br")
            .enqueue(object : Callback<CurrentWeather>{
                override fun onResponse(
                    call: Call<CurrentWeather>,
                    response: Response<CurrentWeather>
                ) {
                    val data = response.body()
                    if(data == null) {
                        Toast.makeText(requireContext(), "Cidade não encontrada", Toast.LENGTH_SHORT).show()
                    } else {
                        val iconId = data.weather[0].icon
                        val imgUrl = "https://openweathermap.org/img/wn/$iconId@2x.png"

                        binding.apply {
                            Picasso.get().load(imgUrl).into(ivWeather)
                            tvCity.text = data.name
                            tvSunrise.text = convertEpochToDataWithOffset(data.sys.sunrise.toLong(), data.timezone)
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                            tvSunset.text = convertEpochToDataWithOffset(data.sys.sunset.toLong(), data.timezone)
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                            tvSky.text = data.weather[0].description
                            tvWind.text = "${data.wind.speed}Km/h"
                            tvFeelsLike.text = "Sensação térmica de ${data.main.feels_like.toInt()}ºC"
                            tvWeather.text = "${data.main.temp.toInt()}ºC"
                            tvMinTemp.text = "Mínima: ${data.main.temp_min.toInt()}ºC"
                            tvMaxTemp.text = "Máxima: ${data.main.temp_max.toInt()}ºC"
                            tvPressure.text = "${data.main.pressure}hPa"
                            tvHumidity.text = "${data.main.humidity}%"
                            tvUpdatedAt.text = "Última atualização: ${convertEpochToDataWithOffset(data.dt.toLong(), data.timezone)
                                .format(DateTimeFormatter.ofPattern("HH:mm"))}"
                        }
                    }
                }

                override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                    Log.e("ErroRetrofit", t.message.toString())
                }
            })
    }

    private fun convertEpochToDataWithOffset(time: Long, timezone: Int): OffsetDateTime {
        val instant = Instant.ofEpochSecond(time)
        return OffsetDateTime.ofInstant(instant, ZoneOffset.ofTotalSeconds(timezone))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}