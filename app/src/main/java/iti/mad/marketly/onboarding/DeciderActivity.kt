package iti.mad.marketly.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.presentation.MainActivity

class DeciderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_decider);
        prepareProperties()
        finish()
    }

    private fun prepareProperties() {
        val seen: Boolean = SharedPreferenceManager.getOnBoardingSeen(this)
        if (seen) {
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        } else {
            startActivity(Intent(this, OnboardingScreenActivity::class.java))
        }
    }
}