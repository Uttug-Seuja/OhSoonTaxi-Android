package com.example.myapplication.ui.common.utils

import android.graphics.Color
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

// 에딧텍스트 텍스트 변경을 flow로 받기
@ExperimentalCoroutinesApi
fun EditText.textChangesToFlow(): Flow<CharSequence?> {

    // flow 콜백 받기
    return callbackFlow<CharSequence?> {

        val listener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun afterTextChanged(p0: Editable?) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                Log.d(TAG, "onTextChanged() / textChangesToFlow() 에 달려있는 텍스트 와쳐 / text : $text")
                // 값 내보내기
                trySend(text).isSuccess
            }
        }
        // 위에서 설정한 리스너 달아주기
        addTextChangedListener(listener)

        // 콜백이 사라질때 실행됨
        awaitClose {
//            Log.d(TAG, "textChangesToFlow() awaitClose 실행")
            removeTextChangedListener(listener)
        }

    }.onStart {
//        Log.d(TAG, "textChangesToFlow() / onStart 발동")
        // Rx 에서 onNext 와 동일
        // emit 으로 이벤트를 전달
        emit(text)
    }

}



object Constants {

    fun textChangeColor(text : TextView, color: String, start : Int, end : Int) : SpannableStringBuilder {

        // 1. SpannableStringBuilder 타입으로 변환
        val builder = SpannableStringBuilder(text.text.toString())

        // 2. index=4에 해당하는 문자열(4)에 색 적용
        builder.setSpan(ForegroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return builder
    }

    fun textChangeStyle(text : TextView, style: StyleSpan, start : Int, end : Int) : SpannableStringBuilder{

        // 1. SpannableStringBuilder 타입으로 변환
        val builder = SpannableStringBuilder(text.text.toString())

        // 2. index=4에 해당하는 문자열(4)에 색 적용
        builder.setSpan(style, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return builder

    }
}
