package com.vsaytech.mvvmweather.ui.dailyforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.vsaytech.mvvmweather.R
import com.vsaytech.mvvmweather.databinding.FragmentDailyForecastBinding

class DailyForecastFragment : Fragment() {

    private var _binding: FragmentDailyForecastBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var hourlyAdapter: HourlyForecastAdapter? = null
    private val args: DailyForecastFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDailyForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val currentWeatherDailyForecast = args.currentWeatherDailyForecast
        val locationName = args.currentLocationName
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.hour_forecast, locationName)

        if (currentWeatherDailyForecast.hourList.isNotEmpty()) {
            hourlyAdapter = HourlyForecastAdapter()
            binding.apply {
                rvHourlyForecast.apply {
                    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                    layoutManager = LinearLayoutManager(context)
                    adapter = hourlyAdapter
                }
                hourlyAdapter?.currentHourlyForecastList = currentWeatherDailyForecast.hourList

                Glide.with(ivMoon).load(R.drawable.ic_moon).into(ivMoon)
                Glide.with(ivSun).load(R.drawable.ic_sun).into(ivSun)

                tvSunRise.text = getString(R.string.sun_rise, currentWeatherDailyForecast.astro.sunrise)
                tvSunSet.text = getString(R.string.sun_set, currentWeatherDailyForecast.astro.sunset)

                tvMoonRise.text = getString(R.string.moon_rise, currentWeatherDailyForecast.astro.moonrise)
                tvMoonSet.text = getString(R.string.moon_set, currentWeatherDailyForecast.astro.moonset)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}