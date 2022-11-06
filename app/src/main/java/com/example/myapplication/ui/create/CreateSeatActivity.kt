package com.example.myapplication.ui.create

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityCreateGenderBinding
import com.example.myapplication.databinding.ActivityCreateSeatBinding

class CreateSeatActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCreateSeatBinding.inflate(layoutInflater) }

    companion object {

        const val INTENT_EXTRA_MSG = "intent.extra.msg"

        fun createIntent(context: Context, msg: String): Intent {
            return Intent(context, CreateSeatActivity::class.java).also {
                it.putExtra(INTENT_EXTRA_MSG, msg)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.passengerSeatBtn.setOnClickListener {
            setResult(RESULT_OK, intent.putExtra("seatType", "보조석"))
            finish()

        }

        binding.backSeatBtn.setOnClickListener {
            setResult(RESULT_OK, intent.putExtra("seatType", "뒷자석"))
            finish()

        }
    }
}