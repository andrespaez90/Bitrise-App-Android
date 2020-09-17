package com.andrespaez.bitrise.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andrespaez.bitrise.R

class BuildsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_builds)
    }
}