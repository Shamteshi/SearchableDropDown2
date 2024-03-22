package com.example.searchabledropdown2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchableDropdownPresenter : ViewModel(){

    var state by mutableStateOf(States())

    fun loadValues(){
        viewModelScope.launch {
            if(state.result.isEmpty()){
                val data = listOf(1,2,3,4,5)
                delay(3000)
                state = state.copy(result = data)
            }

        }

    }


}