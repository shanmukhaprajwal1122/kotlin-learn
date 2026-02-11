package com.example.a1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    // MutableLiveData (internal - we can modify it)
    private val _userInput = MutableLiveData<String>()

    val userInput: LiveData<String> = _userInput

    fun setUserInput(input: String) {
        _userInput.value = input
    }

    fun getUserInput(): String {
        return _userInput.value ?: "No data"
    }
}