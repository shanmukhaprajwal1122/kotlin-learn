package com.example.a1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _userInputList = MutableLiveData<List<String>>(emptyList())
    val userInputList: LiveData<List<String>> = _userInputList

    fun addUserInput(input: String) {
        val currentList = _userInputList.value ?: emptyList()
        val newList = currentList + input
        _userInputList.value = newList
    }

    fun clearHistory() {
        _userInputList.value = emptyList()
    }

    fun getInputAt(index: Int): String? {
        return _userInputList.value?.getOrNull(index)
    }

    fun getInputCount(): Int {
        return _userInputList.value?.size ?: 0
    }
}