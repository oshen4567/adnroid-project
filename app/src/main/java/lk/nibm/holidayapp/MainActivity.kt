package lk.nibm.holidayapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var spinner_Country :Spinner
    lateinit var spinner_Year : Spinner
    lateinit var btn_Search : Button
    lateinit var holidayViewer : RecyclerView

        var countryArrayFullDetails : JSONArray = JSONArray()
        lateinit var countrySpinnerArray : Array<String>
        //drop down years
        var yearSpinnerArray = arrayOf(2023,2022,2021,2020,2019,2018,2017,2016,
        2015,2014,2013,2012,2011,2010,2009,2008,2007,2006,2005,2004,2003,2002,2001,2000)

    var holidayArray = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_Search = findViewById(R.id.btnSearch)
        spinner_Country = findViewById(R.id.spinner_Country)
        spinner_Year = findViewById(R.id.spinner_Year)

        holidayViewer = findViewById(R.id.Holidayviewer)
        holidayViewer.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        holidayViewer.adapter = HolidayAdapter()

        getCountryDetails()

        //setting  values into spinner (spinnerYear)
        val adapterYear = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,yearSpinnerArray)
        spinner_Year.adapter = adapterYear

        btn_Search.setOnClickListener {
            val  country = spinner_Country.selectedItemPosition
            val year = spinner_Year.selectedItem.toString()
            getHolidaydata(countryArrayFullDetails.getJSONObject(country).getString("iso-3166"),year)
        }

    }
    //Getting all the countries and setting those values into spinner (spinnerCountry)
    private fun getCountryDetails(){

        val url = "https://calendarific.com/api/v2/countries?api_key=171f9a9c21a7fa85ff6e7dc52612b6cb8f7adb61"

        val request = StringRequest(Request.Method.GET,url,
            Response.Listener { respose ->
                try{
                    countryArrayFullDetails = JSONObject(respose).getJSONObject("response").getJSONArray("countries")
                    countrySpinnerArray = Array<String>(countryArrayFullDetails.length()){"s"}

                    for(i in 0 until countryArrayFullDetails.length()){
                        countrySpinnerArray[i] = countryArrayFullDetails.getJSONObject(i).getString("country_name")
                    }

                    val adapterCountry = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,countrySpinnerArray)
                    spinner_Country.adapter = adapterCountry
                }
                catch(e : Error){
                    Toast.makeText(this,"Http Response Error!!",Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->

            })
        Volley.newRequestQueue(applicationContext).add(request)
    }

    //Getting all the holidays for the Recycler Viewer
    fun getHolidaydata(country : String ,selectedYear:String){

        val url = "https://calendarific.com/api/v2/holidays?&api_key=a7b72618c0caec4ccc77e19322ad03b566e3a556&country=$country&year=$selectedYear&$"

        val result = StringRequest(Request.Method.GET,url,
        Response.Listener { response ->
            try {
                holidayArray = JSONObject(response).getJSONObject("response").getJSONArray("holidays")
                holidayViewer.adapter?.notifyDataSetChanged()
            }
            catch (e : Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
                ,Response.ErrorListener{ error-> })

        Volley.newRequestQueue(applicationContext).add(result)

    }

    inner class HolidayAdapter : RecyclerView.Adapter<HolidayHolder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayHolder {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.holiday,parent,false)
            return HolidayHolder(view)

        }

        override fun getItemCount(): Int {
        return holidayArray.length()
        }
        //On click events of the Recycler Viewer
        override fun onBindViewHolder(holder: HolidayHolder, position: Int) {
            try {

                val holiday = holidayArray.getJSONObject(position)
                holder.txtname.text = holiday.getString("name")
                holder.txtdate.text = holiday.getString("date")

                holder.txtdate.text = holidayArray.getJSONObject(position).getJSONObject("date").getString("iso")
                holder.txtname.text = holidayArray.getJSONObject(position).getString("name")
                holder.holiday_Touchitem.setOnClickListener{
                    val intent = Intent(this@MainActivity,DetailsView::class.java)
                    intent.putExtra("txtname",holiday.getString("name"))
                    intent.putExtra("txtdes",holiday.getString("description"))

                    intent.putExtra("day", holiday.getJSONObject("date").getJSONObject("datetime").getString("day"))
                    intent.putExtra("month", holiday.getJSONObject("date").getJSONObject("datetime").getString("month"))
                    startActivity(intent)
                }

            }catch (e:Exception){
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()

            }
        }
    }
    //New Class
    inner class HolidayHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val txtdate : TextView =itemView.findViewById(R.id.txtdate)
        val txtname : TextView = itemView.findViewById(R.id.txtname)
        val holiday_Touchitem : LinearLayout = itemView.findViewById(R.id.holiday_Touchitem)
    }


}






