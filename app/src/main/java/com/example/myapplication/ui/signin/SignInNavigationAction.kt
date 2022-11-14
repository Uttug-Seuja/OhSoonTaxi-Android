package com.example.myapplication.ui.signin

sealed class SignInNavigationAction {
    object NavigateToHome: SignInNavigationAction()
    object NavigateToSignup: SignInNavigationAction()
}
