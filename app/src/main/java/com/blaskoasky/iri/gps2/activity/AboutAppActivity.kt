package com.blaskoasky.iri.gps2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blaskoasky.iri.gps2.databinding.ActivityAboutAppBinding

class AboutAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = "About App"
    }
}