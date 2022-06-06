package com.vsaytech.mvvmweather.ui.dailyforecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vsaytech.mvvmweather.R
import com.vsaytech.mvvmweather.data.model.HourWS
import com.vsaytech.mvvmweather.databinding.HourlyForecastItemBinding
import com.vsaytech.mvvmweather.util.getTimeFromDateString

class HourlyForecastAdapter : RecyclerView.Adapter<HourlyForecastViewHolder>() {
    /**
     * The WeatherHourlyForecastList that our Adapter will show
     */
    var currentHourlyForecastList: List<HourWS> = emptyList()
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val itemBinding = HourlyForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyForecastViewHolder(itemBinding)
    }

    override fun getItemCount() = currentHourlyForecastList.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        val paymentBean: HourWS = currentHourlyForecastList[position]
        holder.bind(paymentBean)
    }
}

/**
 * ViewHolder for WeatherHourlyForecast items.
 */
class HourlyForecastViewHolder(private val itemBinding: HourlyForecastItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(hourlyForecast: HourWS) {
        itemBinding.apply {
            tvHourlyTime.text = hourlyForecast.time?.let { getTimeFromDateString(it) }
            Glide.with(ivHourlyConditionIcon.context).load(hourlyForecast.condition?.getHourlyConditionIcon()).into(ivHourlyConditionIcon)
            tvHourlyWeather.text = tvHourlyWeather.context.getString(R.string.current_weather_temp, hourlyForecast.temp_f.toString())
            tvHourlyCondition.text = hourlyForecast.condition?.text
            tvHourlyHumidityValue.text = hourlyForecast.humidity.toString()
            tvHourlyWindValue.text = tvHourlyWindValue.context.getString(R.string.hour_wind_value, hourlyForecast.wind_mph.toString())
            tvHourlyTempFeelLike.text = tvHourlyTempFeelLike.context.getString(R.string.current_weather_temp, hourlyForecast.feelslike_f.toString())
        }
    }
}