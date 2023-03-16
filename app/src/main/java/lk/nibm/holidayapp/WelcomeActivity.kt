package lk.nibm.holidayapp


import android.content.Intent
import android.media.Image
import android.media.tv.TvContract.Channels.Logo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class WelcomeActivity : AppCompatActivity() {

    private lateinit var logo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        logo = findViewById(R.id.logo)
        logo.alpha=0f
        logo.animate().setDuration(2000).alpha(1f).withEndAction{
            val intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }

    }
}