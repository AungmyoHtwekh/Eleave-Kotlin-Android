package com.yoma.yomahackathon.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.yoma.yomahackathon.R
import com.yoma.yomahackathon.adapters.DataAdpter
import com.yoma.yomahackathon.model.LeaveAssociate
import com.yoma.yomahackathon.model.PendingLeave
import com.yoma.yomahackathon.model.PendingLeaveRequest
import com.yoma.yomahackathon.retrofit.ApiClient
import org.threeten.bp.LocalDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AssociateLeaveActivity() : AppCompatActivity() {

    private lateinit var calenderView: MaterialCalendarView
    lateinit var progerssProgressDialog: ProgressDialog
    lateinit var pendingLeave: PendingLeave
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: DataAdpter
    var pendingLeaveList = ArrayList<LeaveAssociate>()
    private lateinit var sharedPref: SharedPreferences
    private var PRIVATE_MODE = 0

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_associate_leave)
        sharedPref = getSharedPreferences(getString(R.string.login_data), PRIVATE_MODE)
        recyclerView = findViewById(R.id.recycler_view)
        calenderView = findViewById(R.id.calendarView)
        calenderView.setSelectedDate(LocalDate.now())
        progerssProgressDialog=ProgressDialog(this)
        progerssProgressDialog.setTitle("Loading")
        progerssProgressDialog.setCancelable(false)
        calenderView.setOnDateChangedListener { widget, date, selected ->
            pendingLeaveList.clear()
            progerssProgressDialog.show()
            val startDate = convertCalenderDay(date)
            getPendingLeaves(startDate,startDate)
        }

        if(!sharedPref.getBoolean(getString(R.string.biometric_login), false)){
            showDialog()
        }else{
            //setting up the adapter
            recyclerView.adapter= DataAdpter(pendingLeaveList,this)
            recyclerView.layoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            progerssProgressDialog.show()
            val startDate = convertCalenderDay(calenderView.selectedDate!!)
            val endDate = calculateDate(calenderView.currentDate!!)
            getPendingLeaves(startDate,endDate[1])
        }

        calenderView.setOnMonthChangedListener { widget, date ->
            pendingLeaveList.clear()
            progerssProgressDialog.show()
            val calculatedDate: Array<String> = calculateDate(date)
            getPendingLeaves(calculatedDate[0], calculatedDate[1])

        }

    }

    fun calculateDate(date: CalendarDay): Array<String>{

        val c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        val dateList = convertCalenderDay(date).split("-")
        c.set(dateList[0].toInt(),dateList[1].toInt() - 1,dateList[2].toInt())
        val df =  SimpleDateFormat("yyyy-MM-dd");
        val startDate = df.format(c.getTime())
        for (i in 1..6) {
            c.add(Calendar.DATE, 1);
        }
        val endDate = df.format(c.getTime())
        return arrayOf(startDate,endDate)
    }

    fun convertCalenderDay(date: CalendarDay): String{
        val selectedDate: CalendarDay = date
        val date: LocalDate = selectedDate.date
        return date.toString()
    }

    fun getPendingLeaves( startDate: String,  endDate: String){
        val call: Call<PendingLeave> = ApiClient.getClient.getPendingLeaveList(PendingLeaveRequest("008076","Applied",startDate,endDate,""),"Bearer " + sharedPref.getString(getString(R.string.access_token),""))
        call.enqueue(object: Callback<PendingLeave>{
            override fun onFailure(call: Call<PendingLeave>, t: Throwable) {
                progerssProgressDialog.dismiss()
            }

            override fun onResponse(call: Call<PendingLeave>, response: Response<PendingLeave>) {
                progerssProgressDialog.dismiss()
                if(response.code() == 200){
                    pendingLeave = response.body()!!
                    pendingLeaveList.addAll(pendingLeave.leaveAssociateList)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                if(response.code() == 401){
                    tokenExipreDialog()
                }
            }
        })
    }

    fun tokenExipreDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Session Expired")
        builder.setMessage("Your session is expired.")
        builder.setPositiveButton("Ok"){dialog, which ->
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Yoma Hackthon")
        builder.setMessage("Do you want to use your biometric data (fingerprint) to log in next time without using account password?")
        builder.setPositiveButton("Do it"){dialog, which ->
            sharedPref.edit().putBoolean(getString(R.string.biometric_login),true).apply()
            showData()
            dialog.dismiss()
        }

        builder.setNegativeButton("No thanks"){dialog,which ->
            showData()
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showData(){
        //setting up the adapter
        recyclerView.adapter= DataAdpter(pendingLeaveList,this)
        recyclerView.layoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        progerssProgressDialog.show()
        val startDate = convertCalenderDay(calenderView.selectedDate!!)
        getPendingLeaves(startDate,startDate)
    }

}