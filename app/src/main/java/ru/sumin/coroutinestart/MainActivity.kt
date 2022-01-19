package ru.sumin.coroutinestart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ru.sumin.coroutinestart.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {



    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //handler без параметров объявлен устаревшим, надо в параметрах указывать в каком потоке хэндлер работает
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLoad.setOnClickListener {
            loadData()
        }

    }

    private fun loadData() {
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false
        loadCity {
            binding.tvLocation.text = it
            loadTemperature(it) {
                binding.tvTemperature.text = it.toString()
                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
            }

        }

    }

    private fun loadCity(callback: (String) -> Unit) {
        thread {
            Thread.sleep(5000)
            handler.post {
                callback.invoke("Moscow")
            }
        }
    }

    private fun loadTemperature(city: String, callback: (Int) -> Unit) {
        thread {
            Looper.prepare()
            // простой вызов хандлера вызовет ошибки, надо вызывать перед ним луупер.препаре
            Handler()
            Handler(Looper.getMainLooper()).post{
                Toast.makeText(
                    this,
                    getString(R.string.loading_temperature_toast, city),
                    Toast.LENGTH_SHORT
                ).show()
            }

            Thread.sleep(5000)
            //runOnUiThread и Handler(Looper.getMainLooper()) равносильны
            runOnUiThread() {
                callback.invoke(17)
            }
        }

    }
}
