package com.example.myapplication.ui.create

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.myapplication.databinding.ActivityCreateGenderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateGenderActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCreateGenderBinding.inflate(layoutInflater) }

    companion object {

        const val INTENT_EXTRA_MSG = "intent.extra.msg"

        fun createIntent(context: Context, msg: String): Intent {
            return Intent(context, CreateGenderActivity::class.java).also {
                it.putExtra(INTENT_EXTRA_MSG, msg)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.genderAllBtn.setOnClickListener {
            setResult(RESULT_OK, intent.putExtra("genderType","남녀 모두"))
            finish()
        }

        binding.genderManBtn.setOnClickListener {
            setResult(RESULT_OK, intent.putExtra("genderType","남자만"))
            finish()
        }

        binding.genderWomanBtn.setOnClickListener {
            setResult(RESULT_OK, intent.putExtra("genderType","여자만"))
            finish()
        }
    }
}