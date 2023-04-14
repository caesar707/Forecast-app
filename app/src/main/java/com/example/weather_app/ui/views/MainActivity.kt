package com.example.weather_app.ui.views

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weather_app.R
import com.example.weather_app.data.models.City
import com.example.weather_app.data.models.Data
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.ui.adapters.ForecastAdapter
import com.example.weather_app.ui.base.BaseActivity
import com.example.weather_app.utils.AppUtils.showToast
import com.example.weather_app.utils.ProgressLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {

    private val viewModel by viewModels<ForecastViewModel>()

    private lateinit var adapter: ForecastAdapter

    private var latitude = 0.0
    private var longitude = 0.0
    private var cityID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = ForecastAdapter()
        binding.rvForecastItems.adapter = adapter

        observers()

        viewModel.getCities()

        binding.btnRetry.setOnClickListener(this)
    }

    private fun observers() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.loadingLiveData.observe(this@MainActivity) {
                    if (it)
                        ProgressLoading.show(this@MainActivity)
                    else
                        ProgressLoading.dismiss()
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.errorLiveData.observe(this@MainActivity) {
                    showToast(getString(it))
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.noRemoteDataLiveData.observe(this@MainActivity) {
                    if (it) {
                        viewModel.getLocalData(cityID)
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.dataNotExistLiveData.observe(this@MainActivity) {
                    if (it) {
                        binding.apply {
                            rvForecastItems.visibility = View.GONE
                            groupNoData.visibility = View.VISIBLE
                        }
                    } else {
                        binding.apply {
                            rvForecastItems.visibility = View.VISIBLE
                            groupNoData.visibility = View.GONE
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.citiesLiveData.observe(this@MainActivity) {
                    updateSpinner(it)
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.forecastLiveData.observe(this@MainActivity, observer)
            }
        }
    }

    private val observer = Observer<List<Data>> {
        adapter.addData(it)
        binding.rvForecastItems.smoothScrollToPosition(0)
    }

    private fun updateSpinner(list: MutableList<City>) {
        list.add(0, City("Select City", "Select City", -1, 0.0, 0.0))
        val adapter = ArrayAdapter(this, R.layout.item_spinner, list)
        binding.apply {
            spinnerCities.adapter = adapter
            spinnerCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, p1: View?, position: Int, p3: Long
                ) {
                    if (position != 0) {
                        latitude = list[position].lat
                        longitude = list[position].lon
                        cityID = list[position].id
                        viewModel.getCityForecast(cityID, latitude, longitude)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onClick(v: View?) {
        viewModel.getCityForecast(cityID, latitude, longitude)
    }
}