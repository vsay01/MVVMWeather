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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.vsaytech.mvvmweather.ui.uistate.NetworkResult
import com.vsaytech.mvvmweather.util.getDayNameFromDateTimeString
import com.vsaytech.mvvmweather.util.getMonthDayFromDateTimeString
import com.vsaytech.mvvmweather.util.getTimeFromDateString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
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
    @Inject
    lateinit var dailyForecastAdapter: DailyForecastAdapter

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel by viewModels<CurrentWeatherViewModel>()

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
        // Start a coroutine in the lifecycle scope
        viewLifecycleOwner.lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    observeCurrentWeatherStateFlow()
                }

                launch {
                    observeDailyWeatherForecastListStateFlow()
                }
            }
        }

        binding.rvDailyForecast.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = dailyForecastAdapter

            dailyForecastAdapter.setOnDailyForecastItemClickLister { currentWeatherDailyForecast ->
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
        }
    }

    private suspend fun observeDailyWeatherForecastListStateFlow() {
        // Trigger the flow and start listening for values.
        // Note that this happens when lifecycle is STARTED and stops
        // collecting when the lifecycle is STOPPED
        viewModel.dailyForecastWeatherListStateFlow.collect { resultNetwork ->
            when (resultNetwork) {
                is NetworkResult.Success -> {
                    resultNetwork.data.let { currentWeatherDailyForecast ->
                        binding.pbLoadingSpinner.visibility = View.GONE
                        currentWeatherDailyForecast.apply {
                            binding.apply {
                                if (currentWeatherDailyForecast.isNotEmpty()) {
                                    tvTempMin.text = getString(R.string.current_weather_temp, get(0).mintemp_f.toString())
                                    tvTempMax.text = getString(R.string.current_weather_temp, get(0).maxtemp_f.toString())
                                    dailyForecastAdapter.currentWeatherDailyForecastList = currentWeatherDailyForecast
                                    tvDailyForecastLabel.visibility = View.VISIBLE
                                    tvDailyForecastDay.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Error -> {
                    //show error message
                    binding.pbLoadingSpinner.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        resultNetwork.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading<*> -> {
                    //show loader, shimmer effect etc
                    binding.pbLoadingSpinner.visibility = View.VISIBLE
                    binding.gpWeatherTopSection.visibility = View.GONE
                }
            }
        }
    }

    private suspend fun observeCurrentWeatherStateFlow() {
        // Trigger the flow and start listening for values.
        // Note that this happens when lifecycle is STARTED and stops
        // collecting when the lifecycle is STOPPED
        viewModel.currentWeatherStateFlow.collect { resultNetwork ->
            when (resultNetwork) {
                is NetworkResult.Success -> {
                    binding.pbLoadingSpinner.visibility = View.GONE
                    resultNetwork.data.let { currentWeather ->
                        currentWeather.apply {
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
                                pbLoadingSpinner.visibility = View.GONE
                            }
                        }
                    }
                }
                is NetworkResult.Error -> {
                    //show error message
                    binding.pbLoadingSpinner.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        resultNetwork.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading<*> -> {
                    //show loader, shimmer effect etc
                    binding.pbLoadingSpinner.visibility = View.VISIBLE
                    binding.gpWeatherTopSection.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}