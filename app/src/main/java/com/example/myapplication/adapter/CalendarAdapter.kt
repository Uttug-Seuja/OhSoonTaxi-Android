package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.CalendarDateModel
import com.example.myapplication.databinding.ItemRecyclerDateBinding
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(val onClickListener: ItemClickListener) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private var items = ArrayList<CalendarDateModel>()
    private val cal = Calendar.getInstance(Locale.KOREA)


    interface ItemClickListener {
        fun onItemClickListener(
            item: CalendarDateModel,
            position: Int,
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecyclerDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.bind(items[position])
    }


    inner class ViewHolder(private val binding: ItemRecyclerDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(calendarDateModel: CalendarDateModel) {

            val calendarDay = binding.tvCalendarDay
            val calendarDate = binding.tvCalendarDate
            val cardView = binding.cardCalendar


            if (calendarDateModel.isSelected) {
                calendarDay.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    )
                )
                calendarDate.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    )
                )
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.calender_picker
                    )
                )
            } else {

                when (calendarDateModel.calendarDay) {

                    "토" -> {
                        calendarDay.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.blue
                            )
                        )
                        calendarDate.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.blue
                            )
                        )

                    }

                    "일" -> {
                        calendarDay.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red
                            )
                        )
                        calendarDate.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red
                            )
                        )
                    }

                    else -> {
                        calendarDay.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.black
                            )
                        )
                        calendarDate.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.black
                            )
                        )

                    }


                }


                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    )
                )
            }

            calendarDay.text = calendarDateModel.calendarDay
            calendarDate.text = calendarDateModel.calendarDate
            cardView.setOnClickListener {
                onClickListener.onItemClickListener(calendarDateModel, adapterPosition)
            }
        }

    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun setItem(item: CalendarDateModel) {

    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    internal fun setData(calendarList: ArrayList<CalendarDateModel>) {

        this.items = calendarList
        notifyDataSetChanged()

    }

}
