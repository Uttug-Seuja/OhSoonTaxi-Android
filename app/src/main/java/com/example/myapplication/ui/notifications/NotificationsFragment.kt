package com.example.myapplication.ui.notifications

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMyInfoBinding
import com.example.myapplication.databinding.FragmentNotificationsBinding
import com.example.myapplication.ui.my_info.MyInfoFragment
import com.example.myapplication.ui.my_info.MyInfoViewModel

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: NotificationsViewModel


    fun newInstance(): NotificationsFragment {
        return NotificationsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ttt", "onCreate 실행")

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)





        return binding.root


    }
}