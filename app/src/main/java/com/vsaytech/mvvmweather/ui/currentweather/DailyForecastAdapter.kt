package com.vsaytech.mvvmweather.ui.currentweather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vsaytech.mvvmweather.databinding.DailyForecastItemBinding
import com.vsaytech.mvvmweather.ui.domain.CurrentWeatherDailyForecast
import com.vsaytech.mvvmweather.util.getDayNameFromDateString
import com.vsaytech.mvvmweather.util.getMonthDayFromDateString

class DailyForecastAdapter(private val dailyForecastClickLister: DailyForecastClickListener) : RecyclerView.Adapter<DailyForecastViewHolder>() {
    /**
     * The WeatherDailyForecastList that our Adapter will show
     */
    var currentWeatherDailyForecastList: List<CurrentWeatherDailyForecast> = emptyList()
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val itemBinding = DailyForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyForecastViewHolder(itemBinding)
    }

    override fun getItemCount() = currentWeatherDailyForecastList.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        val paymentBean: CurrentWeatherDailyForecast = currentWeatherDailyForecastList[position]
        holder.bind(paymentBean, dailyForecastClickLister)
    }
}

/**
 * ViewHolder for WeatherDailyForecast items.
 */
class DailyForecastViewHolder(private val itemBinding: DailyForecastItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(currentWeatherDailyForecast: CurrentWeatherDailyForecast, dailyForecastClickLister: DailyForecastClickListener) {
        itemBinding.apply {
            clDailyForecast.setOnClickListener {
                dailyForecastClickLister.onDailyForecastClicked(currentWeatherDailyForecast)
            }
            tvDay.text = getDayNameFromDateString(currentWeatherDailyForecast.day)
            tvDate.text = getMonthDayFromDateString(currentWeatherDailyForecast.monthDay)
            tvCondition.text = currentWeatherDailyForecast.conditionText
            Glide.with(ivConditionIcon.context).load(currentWeatherDailyForecast.conditionIcon).into(ivConditionIcon)
            tvTempMax.text = currentWeatherDailyForecast.maxtemp_f.toString()
            tvTempMin.text = currentWeatherDailyForecast.mintemp_f.toString()
        }
    }
}