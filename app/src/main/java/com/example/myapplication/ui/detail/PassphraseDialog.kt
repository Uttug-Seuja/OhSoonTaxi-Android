package com.example.myapplication.ui.detail

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.example.myapplication.databinding.ActivityPassphraseDialogBinding

class PassphraseDialog : AppCompatActivity() {
    private val binding by lazy { ActivityPassphraseDialogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.dialog = this
        binding.lifecycleOwner = this
        val challengeWord = intent.getStringExtra("challengeWord")
        val countersignWord = intent.getStringExtra("countersignWord")


        binding.passphraseText.text = "문어에 \"$challengeWord\" 답어에 \"$countersignWord\" 입니다."


        // 1. SpannableStringBuilder 타입으로 변환
        val builder = SpannableStringBuilder(binding.passphraseText.text.toString())

        // 2. 색 적용
        builder.setSpan(ForegroundColorSpan(Color.parseColor("#ea3e42")), 4, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(ForegroundColorSpan(Color.parseColor("#1570ff")), 13, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(StyleSpan(Typeface.BOLD), 4, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(StyleSpan(Typeface.BOLD), 13, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // 3. TextView에 적용
        binding.passphraseText.text = builder

    }
}