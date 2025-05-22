import android.content.Context
import android.media.MediaScannerConnection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micahnyabuto.statussaver.data.local.StatusEntity
import com.micahnyabuto.statussaver.data.repository.StatusRepository
import com.micahnyabuto.statussaver.utils.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val statusRepository: StatusRepository
) : ViewModel() {

    private val _imageStatuses = MutableStateFlow<List<StatusEntity>>(emptyList())
    val imageStatuses: StateFlow<List<StatusEntity>> = _imageStatuses.asStateFlow()

    private val _videoStatuses = MutableStateFlow<List<StatusEntity>>(emptyList())
    val videoStatuses: StateFlow<List<StatusEntity>> = _videoStatuses.asStateFlow()

    private val _savedStatuses = MutableStateFlow<List<StatusEntity>>(emptyList())
    val savedStatuses: StateFlow<List<StatusEntity>> = _savedStatuses.asStateFlow()

    private val _loadingState = MutableStateFlow<UiState>(UiState.Loading)
    val loadingState: StateFlow<UiState> = _loadingState.asStateFlow()

    init {
        observeSavedStatuses()
    }



    fun loadStatusesFromStorage() {
        viewModelScope.launch {
            _loadingState.value = UiState.Loading
            try {
                val statuses = statusRepository.fetchStatusesFromStorage()
                _imageStatuses.value = statuses.filter { it.fileType == "image" }
                _videoStatuses.value = statuses.filter { it.fileType == "video" }
                _loadingState.value = UiState.Success
            } catch (e: Exception) {
                _loadingState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun observeSavedStatuses() {
        statusRepository.getAllSavedStatus()
            .onEach { statuses ->
                _savedStatuses.update { statuses }
            }.catch { e ->
                _loadingState.value = UiState.Error(e.message ?: "Error loading saved statuses")
            }
            .launchIn(viewModelScope)
    }

    fun saveStatus(status: StatusEntity) {
        viewModelScope.launch {
            try {
                statusRepository.saveStatus(status)
            } catch (e: Exception) {
                _loadingState.value = UiState.Error(e.message ?: "Error saving status")
            }
        }
    }
    fun downloadStatus(context: Context, statusFile: File, fileType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedFile = FileUtils.saveStatusToInternalStorage(context, statusFile, fileType)

            savedFile?.let {
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(it.absolutePath),
                    null,
                    null
                )
                val status = StatusEntity(
                    filePath = it.absolutePath,
                    fileType = fileType,
                    dateSaved = System.currentTimeMillis()
                )
                statusRepository.saveStatus(status)
            }
        }
    }


}

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
}