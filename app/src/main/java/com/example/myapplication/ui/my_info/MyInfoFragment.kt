package com.example.myapplication.ui.my_info

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.databinding.FragmentMyInfoBinding
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.ui.search.SearchViewModel

class MyInfoFragment : Fragment() {

    private var _binding: FragmentMyInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy { ViewModelProvider(this, MyInfoViewModel.Factory(Application()))[MyInfoViewModel::class.java] }


    fun newInstance(): MyInfoFragment {
        return MyInfoFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ttt", "onCreate 실행")

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentMyInfoBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel





        return binding.root


    }
}