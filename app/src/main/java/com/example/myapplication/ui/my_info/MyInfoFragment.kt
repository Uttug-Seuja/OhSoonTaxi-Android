package com.example.myapplication.ui.my_info

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.ui.common.GlobalApplication
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.databinding.FragmentMyInfoBinding
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.home.HomeNavigationAction
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.ui.my_info.my_create.MyCreateActivity
import com.example.myapplication.ui.my_info.my_interest.MyInterestActivity
import com.example.myapplication.ui.my_info.my_usage.MyUsageActivity
import com.example.myapplication.ui.search.SearchActivity
import com.example.myapplication.ui.search.SearchViewModel
import com.example.myapplication.ui.signin.SigninActivity
import kotlinx.coroutines.launch

class MyInfoFragment : Fragment() {

    private var _binding: FragmentMyInfoBinding? = null
    private val binding get() = _binding!!
    private var userUid: String? = null
    private val sex =
        hashMapOf<String, String>("MAN" to "남자", "WOMAN" to "여자")
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MyInfoViewModel.Factory(Application())
        )[MyInfoViewModel::class.java]
    }


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
        binding.viewmodel = viewModel

        lifecycleScope.launch {
            GlobalApplication.getInstance().getDataStore().userUid.collect { it ->
                userUid = it

            }
        }

        viewModel.retrofitGetUserMyInfo(userUid!!)

        binding.logoutBtn.setOnClickListener {
            viewModel.postUserSignOutRetrofit(userUid!!)
            lifecycleScope.launch {
                GlobalApplication.getInstance().getDataStore()
                    .setUserUid("")

            }

            lifecycleScope.launch {
                GlobalApplication.getInstance().getDataStore()
                    .setAuthorization("")

            }

            lifecycleScope.launch {
                GlobalApplication.getInstance().getDataStore()
                    .setRefreshToken("")

            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.retrofitGetMyInfoEvent.collect {
                binding.titleText.text = it.myInfoResponseData.name
                binding.subTitleText.text =
                    "${sex[it.myInfoResponseData.sex]} #${it.myInfoResponseData.schoolNum}"
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.navigationEvent.collect {
                when (it) {
                    is MyInfoNavigationAction.NavigateToMyCreate -> {
                        val intent = Intent(activity, MyCreateActivity::class.java)
                        startActivity(intent)
                    }
                    is MyInfoNavigationAction.NavigateToMyUsage -> {
                        val intent = Intent(activity, MyUsageActivity::class.java)
                        startActivity(intent)
                    }
                    is MyInfoNavigationAction.NavigateToMyInterest -> {
                        val intent = Intent(activity, MyInterestActivity::class.java)
                        startActivity(intent)
                    }
                    is MyInfoNavigationAction.NavigateToSignIn -> {
                        val intent = Intent(activity, SigninActivity::class.java)
                        startActivity(intent)
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.remove(this@MyInfoFragment)?.commit()
                    }
                }
            }
        }

        return binding.root

    }
}