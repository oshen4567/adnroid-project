package lk.nibm.holidayapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DetailsView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_view)

        val nameTextView = findViewById<TextView>(R.id.titleHoliday)
        val txtname = intent.getStringExtra("txtname")
        nameTextView.setText(txtname)

        val dateTextView = findViewById<TextView>(R.id.dateHoliday)
        val txtdate = intent.getStringExtra("txtdate")
        dateTextView.setText(txtdate)

        val monthText = findViewById<TextView>(R.id.txtmonth)
        val txtmonth = intent.getStringExtra("month")
        monthText.setText(txtmonth)

        val txtdess = findViewById<TextView>(R.id.txtdes)
        val des = intent.getStringExtra("txtdes")
        txtdess.setText(des)





    }

}