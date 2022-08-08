package com.blaskoasky.iri.gps2.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blaskoasky.iri.gps2.databinding.ActivityAboutAppBinding

class AboutAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Tentang Aplikasi"

        binding.imgInstagram.setOnClickListener {
            val instagram = "https://www.instagram.com/iriantomauduta/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagram))
            startActivity(intent)
        }

        binding.imgLinkedin.setOnClickListener {
            val linkedin = "https://www.linkedin.com/in/iriantomauduta/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedin))
            startActivity(intent)
        }

        binding.imgGithub.setOnClickListener {
            val github = "https://github.com/blaskoasky/GPS-Project"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(github))
            startActivity(intent)
        }
    }
}