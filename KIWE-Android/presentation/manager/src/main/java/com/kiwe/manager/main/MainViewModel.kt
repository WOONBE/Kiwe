package com.kiwe.manager.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwe.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val singUpUseCase: SignUpUseCase,
    ) : ViewModel() {
        fun singUp() {
            viewModelScope.launch {
                singUpUseCase.invoke()
            }
        }
    }
