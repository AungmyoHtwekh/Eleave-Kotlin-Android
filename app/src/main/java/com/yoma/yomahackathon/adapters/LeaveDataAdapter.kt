package com.yoma.yomahackathon.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yoma.yomahackathon.R
import com.yoma.yomahackathon.model.LeaveAssociate
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.*


class DataAdpter(private var dataList: List<LeaveAssociate>, private val context: Context) :
    RecyclerView.Adapter<DataAdpter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_leave,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = dataList.get(position)

        holder.nameTextView.text = dataModel.employeeName
        holder.idTextView.text = "ID: " + dataModel.employeeId
        var leaveType: String
        if (dataModel.tyeCode == "AL"){
            leaveType = "Annual Leave"
        }else if (dataModel.tyeCode == "CL"){
            leaveType = "Casual Leave"
        }else if (dataModel.tyeCode == "ML"){
            leaveType = "Maternity Leave"
        }else if (dataModel.tyeCode == "SL"){
            leaveType = "Sick Leave"
        }else if (dataModel.tyeCode == "DL"){
            leaveType = "Duty Leave"
        }else{
            leaveType = "Paternity Leave"
        }
        var status: String
        if (dataModel.description == "Applied"){
            status = "Processing"
        }else if(dataModel.description == "Rejected"){
            status = "Rejected"
        }else{
            status = "Approved"
        }
        var startDate: Array<String> = convertDate(dataModel.startDate)
        var endDate: Array<String> = convertDate(dataModel.endDate)
        holder.leaveTypeTextView.text = leaveType
        holder.leaveDayTextView.text =  round(dataModel.duration.toDouble()).toString() + " day(s)"
        holder.leaveDateTextView.text = startDate[1] + " " + startDate[0] + " - " + endDate[1] + " " + endDate[0] + ", " + startDate[2]
        holder.leaveStatusTextView.text = status
    }


    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        lateinit var nameTextView: TextView
        lateinit var idTextView: TextView
        lateinit var leaveTypeTextView: TextView
        lateinit var leaveDayTextView: TextView
        lateinit var leaveDateTextView: TextView
        lateinit var leaveStatusTextView: TextView

        init {
            nameTextView = itemLayoutView.findViewById(R.id.name_text)
            idTextView = itemLayoutView.findViewById(R.id.id_text)
            leaveTypeTextView = itemLayoutView.findViewById(R.id.leave_text)
            leaveDayTextView = itemLayoutView.findViewById(R.id.day_text)
            leaveDateTextView = itemLayoutView.findViewById(R.id.date_text)
            leaveStatusTextView = itemLayoutView.findViewById(R.id.status_text)

        }

    }

    fun convertDate(stringDate: String): Array<String>{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formatNowDay = SimpleDateFormat("dd")
        val formatNowMonth = SimpleDateFormat("MMM")
        val formatNowYear = SimpleDateFormat("yyyy")
        val date: Date = dateFormat.parse(stringDate.split("T")[0])
        var dayMonthYear: Array<String> = arrayOf(formatNowDay.format(date),formatNowMonth.format(date),formatNowYear.format(date))
        return dayMonthYear
    }

}