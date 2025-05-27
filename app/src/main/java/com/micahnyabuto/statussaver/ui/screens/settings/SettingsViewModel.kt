package com.micahnyabuto.statussaver.ui.screens.settings

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micahnyabuto.statussaver.data.datastore.ThemePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel@Inject constructor(application: Application): AndroidViewModel(application) {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    init {
        viewModelScope.launch {
            ThemePreference.getThemePreference(context).collect {
                _isDarkMode.value = it
            }
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            ThemePreference.saveThemePreference(context, isDark)
            _isDarkMode.value = isDark
        }
    }
}