package com.mvi.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mvi.example.databinding.ActivityMainBinding
import com.mvi.example.view.MainContract
import com.mvi.example.view.MainViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)

    private val viewModel : MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initObservers()
        binding.generateNumber.setOnClickListener {
            viewModel.setEvent(MainContract.Event.OnRandomNumberClicked)
        }
        binding.showToast.setOnClickListener {
            viewModel.setEvent(MainContract.Event.OnShowToastClicked)
        }
        binding.secondActivity.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }

    /**
     * Initialize Observers
     */
    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it.randomNumberState) {
                    is MainContract.RandomNumberState.Idle -> { binding.progressBar.isVisible = false }
                    is MainContract.RandomNumberState.Loading -> { binding.progressBar.isVisible = true }
                    is MainContract.RandomNumberState.Success -> {
                        binding.progressBar.isVisible = false
                        binding.number.text = it.randomNumberState.number.toString()
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effect.collect {
                when (it) {
                    is MainContract.Effect.ShowToast -> {
                        binding.progressBar.isVisible = false
                        showToast("Error, number is even")
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}

class SecondActivity : AppCompatActivity(R.layout.activity_second)