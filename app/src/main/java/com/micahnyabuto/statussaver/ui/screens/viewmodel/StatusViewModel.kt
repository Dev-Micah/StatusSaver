

package com.micahnyabuto.statussaver.ui.screens.viewmodel
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micahnyabuto.statussaver.data.local.StatusEntity
import com.micahnyabuto.statussaver.data.repository.StatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val statusRepository: StatusRepository,
) : ViewModel() {

    private val _imageStatuses = MutableStateFlow<List<StatusEntity>>(emptyList())
    val imageStatuses: StateFlow<List<StatusEntity>> = _imageStatuses.asStateFlow()

    private val _videoStatuses = MutableStateFlow<List<StatusEntity>>(emptyList())
    val videoStatuses: StateFlow<List<StatusEntity>> = _videoStatuses.asStateFlow()

    private val _safUri = MutableStateFlow<Uri?>(null)
    val safUri: StateFlow<Uri?> = _safUri


    private val _statuses = MutableStateFlow<List<StatusEntity>>(emptyList())
    val statuses: StateFlow<List<StatusEntity>> = _statuses.asStateFlow()


    private val _folderUri = MutableStateFlow<Uri?>(null)
    val folderUri: StateFlow<Uri?> = _folderUri

    fun setFolderUri(uri: Uri) {
        _folderUri.value = uri
    }


    // Loads statuses from device storage (SAF or legacy)
    fun loadStatusesFromStorage() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = statusRepository.fetchStatusesFromStorage()
            _statuses.value = result
        }
    }

    // Get saved statuses from Room DB as Flow (e.g. for Saved screen)
    val savedStatuses = statusRepository.getAllSavedStatus()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Save SAF Uri persistently (call from folder picker)
    fun saveSafUri(uri: Uri) {
        _safUri.value =uri

    }

    // Save a status entity to DB
    fun saveStatus(status: StatusEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            statusRepository.saveStatus(status)
        }
    }

}

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
}