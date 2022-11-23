package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.CalendarDateModel
import com.example.myapplication.data.ReservesListResponseData
import com.example.myapplication.databinding.ItemRecyclerPromiseBinding

class MyReservesAdapter (val onClickListener: ItemClickListener, val context: Context) :
    RecyclerView.Adapter<MyReservesAdapter.ViewHolder>() {
    private var items = ArrayList<ReservesListResponseData>()
    private val gender =
        hashMapOf<String, String>("ALL" to "남녀모두", "MAN" to "남자만", "WOMAN" to "여자만")
    private val reserveStatus =
        hashMapOf<String, String>("POSSIBLE" to "신청가능", "IMMINENT" to "마감임박!", "DEADLINE" to "마감")
    private val stateTextColor = hashMapOf<String, String>(
        "POSSIBLE" to "#FFFFFF",
        "IMMINENT" to "#FFFFFF",
        "DEADLINE" to "#cccccc"
    )
    private val stateBtnColor = hashMapOf<String, String>(
        "POSSIBLE" to "#1570ff",
        "IMMINENT" to "#FF4D37",
        "DEADLINE" to "#EEEEEE"
    )

    interface ItemClickListener {
        fun onItemClickListener(
            item: ReservesListResponseData,
            position: Int,
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecyclerPromiseBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.bind(items[position])
        holder.clickItem(items[position])
    }

    inner class ViewHolder(private val binding: ItemRecyclerPromiseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reservesListResponseData: ReservesListResponseData) {
            binding.startTimeText.text = reservesListResponseData.reservationTime.substring(0, 5)
            binding.titleText.text = reservesListResponseData.title
            binding.subTitleText.text =
                "${reservesListResponseData.startingPlace}->${reservesListResponseData.destination}•${gender[reservesListResponseData.sex]}"
            binding.stateBtn.setCardBackgroundColor(Color.parseColor(stateBtnColor[reservesListResponseData.reservationStatus]))
            binding.stateText.text = reserveStatus[reservesListResponseData.reservationStatus]
            binding.stateText.setTextColor(Color.parseColor(stateTextColor[reservesListResponseData.reservationStatus]))
        }

        fun clickItem(reservesListResponseData: ReservesListResponseData) {
            binding.matchCardView.setOnClickListener {
                onClickListener.onItemClickListener(reservesListResponseData, adapterPosition)
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun setItem(item: CalendarDateModel) {}

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    internal fun setData(reservesListResponse: ArrayList<ReservesListResponseData>) {

        this.items = reservesListResponse
        notifyDataSetChanged()

    }
}