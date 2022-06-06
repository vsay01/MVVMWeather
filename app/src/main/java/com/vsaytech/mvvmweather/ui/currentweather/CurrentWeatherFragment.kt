package com.vsaytech.mvvmweather.ui.currentweather

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vsaytech.mvvmweather.R
import com.vsaytech.mvvmweather.databinding.FragmentCurrentWeatherBinding
import com.vsaytech.mvvmweather.extensions.getLastLocation
import com.vsaytech.mvvmweather.extensions.locationPermissions
import com.vsaytech.mvvmweather.ui.domain.CurrentWeather
import com.vsaytech.mvvmweather.ui.domain.CurrentWeatherDailyForecast
import com.vsaytech.mvvmweather.util.getDayNameFromDateTimeString
import com.vsaytech.mvvmweather.util.getMonthDayFromDateTimeString
import com.vsaytech.mvvmweather.util.getTimeFromDateString

class CurrentWeatherFragment : Fragment() {
    private val requestLocationMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val granted = it.value
                val permission = it.key
                if (!granted) {
                    val neverAskAgain = !ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        permission
                    )
                    if (neverAskAgain) {
                        //user click "never ask again"
                    } else {
                        //show explain dialog
                    }
                    return@registerForActivityResult
                } else {
                    getLocation()
                }
            }
        }


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * RecyclerView Adapter for converting a list of DailyForecast.
     */
    private var dailyForecastAdapter: DailyForecastAdapter? = null

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel: CurrentWeatherViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, CurrentWeatherViewModel.Factory(activity.application))[CurrentWeatherViewModel::class.java]
    }

    private var _binding: FragmentCurrentWeatherBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manageLastLocation()
        initView()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    private fun manageLastLocation() {
        activity?.let { fusedLocationClient = LocationServices.getFusedLocationProviderClient(it) }
        getLastLocation(fusedLocationClient, object : LastLocationListener {
            override fun onLastLocationFound(location: Location) {
                viewModel.refreshCurrentWeatherFromRepository("${location.latitude},${location.longitude}")
            }

            override fun onCheckPermissonFailed() {
                requestLocationMultiplePermissions.launch(locationPermissions)
            }
        })
    }

    private fun initView() {
        viewModel.currentWeather.observe(viewLifecycleOwner) { currentWeather ->
            currentWeather?.apply {
                binding.apply {
                    tvCity.text = name
                    tvDayTime.text = getString(
                        R.string.current_weather_day_month_time,
                        getDayNameFromDateTimeString(currentDayTime),
                        getMonthDayFromDateTimeString(currentDayTime),
                        getTimeFromDateString(currentDayTime)
                    )
                    tvCondition.text = conditionText
                    activity?.let { Glide.with(it).load(conditionIcon).into(ivCondition) }
                    tvCurrentTemp.text = getString(R.string.current_weather_temp, temp_f.toString())
                    tvTempFeelLike.text = getString(R.string.current_weather_temp, feelslike_f.toString())
                    gpWeatherTopSection.visibility = View.VISIBLE
                }
            }
        }
        viewModel.currentWeatherDailyForecastList.observe(viewLifecycleOwner) { currentWeatherDailyForecast ->
            currentWeatherDailyForecast?.apply {
                binding.apply {
                    if (currentWeatherDailyForecast.isNotEmpty()) {
                        tvTempMin.text = getString(R.string.current_weather_temp, get(0).mintemp_f.toString())
                        tvTempMax.text = getString(R.string.current_weather_temp, get(0).maxtemp_f.toString())
                        dailyForecastAdapter?.currentWeatherDailyForecastList = currentWeatherDailyForecast
                        tvDailyForecastLabel.visibility = View.VISIBLE
                        tvDailyForecastDay.visibility = View.VISIBLE
                    }
                }
            }
        }

        // Observer for the network error.
        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) {
                binding.gpWeatherTopSection.visibility = View.GONE
                onNetworkError()
            }
            hideIfNetworkError(binding.pbLoadingSpinner, isNetworkError, viewModel.currentWeather.value)
        }

        //DailyForecast List
        dailyForecastAdapter = DailyForecastAdapter(object : DailyForecastClickListener {
            override fun onDailyForecastClicked(currentWeatherDailyForecast: CurrentWeatherDailyForecast) {
                val options = navOptions {
                    anim {
                        enter = R.anim.slide_in_right
                        exit = R.anim.slide_out_left
                        popEnter = R.anim.slide_in_left
                        popExit = R.anim.slide_out_right
                    }
                }
                this@CurrentWeatherFragment.findNavController()
                    .navigate(
                        CurrentWeatherFragmentDirections.actionCurrentWeatherToDailyForecastFragment(currentWeatherDailyForecast)
                            .setCurrentLocationName(binding.tvCity.text.toString()),
                        options
                    )
            }
        })
        binding.rvDailyForecast.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = dailyForecastAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Method for displaying a Toast error message for network errors.
     */
    private fun onNetworkError() {
        if (viewModel.isNetworkErrorShown.value == false) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    private fun hideIfNetworkError(view: View, isNetWorkError: Boolean, currentWeather: CurrentWeather?) {
        view.visibility = if (currentWeather != null) View.GONE else View.VISIBLE

        if (isNetWorkError) {
            view.visibility = View.GONE
        }
    }
}