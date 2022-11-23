package com.example.myapplication.ui.my_info

sealed class MyInfoNavigationAction {
    object NavigateToMyCreate: MyInfoNavigationAction()
    object NavigateToMyUsage: MyInfoNavigationAction()
    object NavigateToMyInterest: MyInfoNavigationAction()
    object NavigateToSignIn: MyInfoNavigationAction()


}