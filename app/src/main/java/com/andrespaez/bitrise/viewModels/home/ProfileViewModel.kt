package com.andrespaez.bitrise.viewModels.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrespaez.bitrise.viewModels.AndroidViewModel

class ProfileViewModel @ViewModelInject constructor() : AndroidViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}