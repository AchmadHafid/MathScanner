package io.github.achmadhafid.mathscanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.achmadhafid.mathscanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
    }

}
