package iti.mad.marketly.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import iti.mad.marketly.presentation.MainActivity
import iti.mad.marketly.presentation.auth.AuthActivity

class DeciderActivity : AppCompatActivity() {

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_decider);
        prepareProperties()
        finish()
    }

    private fun prepareProperties() {
        val Shared: SharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE)
        val seen: Boolean = Shared.getBoolean("seen", false)
        // Log.d("seen",String.valueOf(seen));
        if (seen) {
            val currentUser: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
            if (currentUser != null && currentUser.isEmailVerified()) {
                //user signed in
                startActivity(Intent(this, MainActivity::class.java))
                this.finish()
            } else {
                startActivity(Intent(this, AuthActivity::class.java))
            }
        } else {
            startActivity(Intent(this, OnboardingScreenActivity::class.java))
        }
    }

    companion object {
        @JvmField
        var MYPREFERENCES = "androidx.appcompat.app.SHARED_PREF"
    }
}