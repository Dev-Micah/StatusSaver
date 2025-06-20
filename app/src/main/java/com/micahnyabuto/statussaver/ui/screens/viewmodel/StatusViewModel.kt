package com.micahnyabuto.statussaver.ui.screens.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micahnyabuto.statussaver.data.datastore.StatusPreferences
import com.micahnyabuto.statussaver.data.local.StatusEntity
import com.micahnyabuto.statussaver.data.repository.StatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val statusRepository: StatusRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _statuses = MutableStateFlow<List<StatusEntity>>(emptyList())
    val statuses: StateFlow<List<StatusEntity>> = _statuses.asStateFlow()

    private val _needsFolderPermission = MutableStateFlow(true)
    val needsFolderPermission: StateFlow<Boolean> = _needsFolderPermission.asStateFlow()

    private val _downloadProgress = MutableStateFlow<Map<String, Float>>(emptyMap())
    val downloadProgress: StateFlow<Map<String, Float>> = _downloadProgress.asStateFlow()

    val imageStatuses = statuses.map { list ->
        list.filter { it.fileType == "image" }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val videoStatuses = statuses.map { list ->
        list.filter { it.fileType == "video" }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val savedStatuses = statusRepository.getAllSavedStatus()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        checkFolderPermissions()
    }

    private fun checkFolderPermissions() {
        viewModelScope.launch {
            val hasWhatsAppUri = StatusPreferences.getWhatsAppUri(context).firstOrNull() != null
            val hasBusinessUri = StatusPreferences.getWhatsAppBusinessUri(context).firstOrNull() != null
            _needsFolderPermission.value = !hasWhatsAppUri && !hasBusinessUri

            if (!_needsFolderPermission.value) {
                loadStatusesFromStorage()
            }
        }
    }

    fun loadStatusesFromStorage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = statusRepository.fetchStatusesFromStorage()
                _statuses.value = result
            } catch (e: Exception) {
                e.printStackTrace()
                showNotification("Error loading statuses")
            }
        }
    }

    suspend fun saveSafUri(uri: Uri) {
        statusRepository.saveSafUri(uri)
        checkFolderPermissions()
    }

    fun saveStatus(status: StatusEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateDownloadProgress(status.filePath, 0f)
                val savedFile = statusRepository.copyStatusToLocal(status) { progress ->
                    updateDownloadProgress(status.filePath, progress)
                }
                val savedStatus = status.copy(filePath = savedFile.absolutePath)
                statusRepository.saveStatus(savedStatus)
                showNotification("Saved successfully")
                updateDownloadProgress(status.filePath, null)
            } catch (e: Exception) {
                e.printStackTrace()
                showNotification("Failed to save")
                updateDownloadProgress(status.filePath, null)
            }
        }
    }

    fun deleteStatus(status: StatusEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                statusRepository.deleteStatus(status)
                showNotification("Deleted successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                showNotification("Failed to delete ")
            }
        }
    }

    private fun updateDownloadProgress(filePath: String, progress: Float?) {
        _downloadProgress.value = if (progress == null) {
            _downloadProgress.value - filePath
        } else {
            _downloadProgress.value + (filePath to progress)
        }
    }

    private fun showNotification(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}