package org.nunocky.myfirebasenoteapp.ui.screens.resetpassword

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.nunocky.myfirebasenoteapp.domain.Authentication
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    val authentication: Authentication,
) : ViewModel() {
}