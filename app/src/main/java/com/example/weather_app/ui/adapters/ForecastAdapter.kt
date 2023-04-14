package com.example.weather_app.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather_app.data.models.Data
import com.example.weather_app.databinding.ItemForecastBinding
import com.example.weather_app.utils.AppUtils.formatDate

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    val data = mutableListOf<Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(data[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(listData: List<Data>) {
        data.clear()
        data.addAll(listData)
        notifyDataSetChanged()
    }

    class ForecastViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: Data) {
            binding.apply {
                tvTemp.text = data.main.temp.toInt().toString() + " \u2103"
                tvDate.text = data.dt_txt.formatDate()
                tvDesc.text = data.weather[0].description
                Glide.with(ivDesc.context)
                    .load("https://openweathermap.org/img/wn/${data.weather[0].icon}.png")
                    .into(ivDesc)
                pressureLayout.tvPressure.text = data.main.pressure.toString()
                humidityLayout.tvHumidity.text = data.main.humidity.toString()
                windLayout.tvWindSpeed.text =
                    ((data.wind.speed * 60 * 60) / 1000).toInt().toString()
            }
        }

    }
}