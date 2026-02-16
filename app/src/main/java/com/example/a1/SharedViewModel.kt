package com.example.a1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _userInputList = MutableLiveData<List<String>>(emptyList())
    val userInputList: LiveData<List<String>> = _userInputList

    // Add new input
    fun addUserInput(input: String) {
        val currentList = _userInputList.value ?: emptyList()
        val newList = currentList + input
        _userInputList.value = newList
    }

    // Edit existing input at position
    fun editUserInput(position: Int, newText: String) {
        val currentList = _userInputList.value ?: emptyList()
        if (position in currentList.indices) {
            val newList = currentList.toMutableList()
            newList[position] = newText
            _userInputList.value = newList
        }
    }

    // Delete input at position
    fun deleteUserInput(position: Int) {
        val currentList = _userInputList.value ?: emptyList()
        if (position in currentList.indices) {
            val newList = currentList.toMutableList()
            newList.removeAt(position)
            _userInputList.value = newList
        }
    }

    // Clear all history
    fun clearHistory() {
        _userInputList.value = emptyList()
    }

    // Get input at position
    fun getInputAt(index: Int): String? {
        return _userInputList.value?.getOrNull(index)
    }

    // Get count
    fun getInputCount(): Int {
        return _userInputList.value?.size ?: 0
    }
}