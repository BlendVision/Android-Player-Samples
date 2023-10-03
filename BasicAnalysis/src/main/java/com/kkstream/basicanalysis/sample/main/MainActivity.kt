package com.kkstream.basicanalysis.sample.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kkstream.basicanalysis.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Example usage: send an event
        val eventName = "player_play"
        val properties = mapOf("custom_parameter1" to "I am String", "custom_parameter2" to 999)

        mainViewModel.sendEvent(eventName, properties)

    }

}