package com.micahnyabuto.statussaver.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.micahnyabuto.statussaver.data.datastore.StatusPreferences
import com.micahnyabuto.statussaver.data.local.StatusDao
import com.micahnyabuto.statussaver.data.local.StatusEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.io.File
import javax.inject.Inject

interface StatusRepository {
    suspend fun fetchStatusesFromStorage(): List<StatusEntity>
    suspend fun saveStatus(status: StatusEntity)
    fun getAllSavedStatus(): Flow<List<StatusEntity>>
    suspend fun saveSafUri(uri: Uri)
    suspend fun copyStatusToLocal(status: StatusEntity, progressCallback: (Float) -> Unit): File
    suspend fun deleteStatus(status: StatusEntity)
}

class StatusRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dao: StatusDao
) : StatusRepository {

    override suspend fun fetchStatusesFromStorage(): List<StatusEntity> {
        val allStatuses = mutableListOf<StatusEntity>()

        /*
        legacy storage first
        */
        val legacyStatuses = fetchStatusesFromLegacyStorage()
        allStatuses.addAll(legacyStatuses)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val safStatuses = fetchStatusesFromSAF()
            // Add only unique statuses from SAF
            safStatuses.forEach { safStatus ->
                if (!allStatuses.any { it.filePath == safStatus.filePath }) {
                    allStatuses.add(safStatus)
                }
            }
        }

        return allStatuses.sortedByDescending { it.dateSaved }
    }

    private fun fetchStatusesFromLegacyStorage(): List<StatusEntity> {
        val whatsappDirs = listOf(
            // WhatsApp paths
            File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/.Statuses"),
            File(Environment.getExternalStorageDirectory(), "Android/media/com.whatsapp/WhatsApp/Media/.Statuses"),
            // WhatsApp Business paths
            File(Environment.getExternalStorageDirectory(), "WhatsApp Business/Media/.Statuses"),
            File(Environment.getExternalStorageDirectory(), "Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses")
        )

        return whatsappDirs
            .filter { it.exists() && it.isDirectory }
            .flatMap { statusDir ->
                statusDir.listFiles()
                    ?.filter { file ->
                        val name = file.name.lowercase()
                        name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                                name.endsWith(".mp4") || name.endsWith(".gif")
                    }
                    ?.map { file ->
                        StatusEntity(
                            filePath = file.absolutePath,
                            fileType = when {
                                file.name.lowercase().endsWith(".mp4") -> "video"
                                else -> "image"
                            },
                            dateSaved = file.lastModified()
                        )
                    } ?: emptyList()
            }
    }

    private suspend fun fetchStatusesFromSAF(): List<StatusEntity> {
        val whatsAppUri = StatusPreferences.getWhatsAppUri(context).firstOrNull()?.toUri()
        val businessUri = StatusPreferences.getWhatsAppBusinessUri(context).firstOrNull()?.toUri()

        return listOfNotNull(whatsAppUri, businessUri).flatMap { uri ->
            try {
                val documentFile = DocumentFile.fromTreeUri(context, uri) ?: return@flatMap emptyList()

                documentFile.listFiles()
                    .filter { file ->
                        val name = file.name?.lowercase() ?: return@filter false
                        name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                                name.endsWith(".mp4") || name.endsWith(".gif")
                    }
                    .mapNotNull { file ->
                        StatusEntity(
                            filePath = file.uri.toString(),
                            fileType = when {
                                file.name?.lowercase()?.endsWith(".mp4") == true -> "video"
                                else -> "image"
                            },
                            dateSaved = file.lastModified()
                        )
                    }
            } catch (e: Exception) {
                Log.e("StatusRepository", "Error reading from SAF URI: $uri", e)
                emptyList()
            }
        }
    }

    override suspend fun saveSafUri(uri: Uri) {
        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )

            val isWhatsAppBusiness = uri.path?.contains("whatsapp.w4b") == true ||
                    uri.path?.contains("WhatsApp Business") == true

            if (isWhatsAppBusiness) {
                StatusPreferences.saveWhatsAppBusinessUri(context, uri.toString())
            } else {
                StatusPreferences.saveWhatsAppUri(context, uri.toString())
            }
        } catch (e: Exception) {
            Log.e("StatusRepository", "Error saving SAF URI", e)
        }
    }

    override suspend fun copyStatusToLocal(status: StatusEntity, progressCallback: (Float) -> Unit): File {
        val sourceUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            status.filePath.toUri()
        } else {
            Uri.fromFile(File(status.filePath))
        }

        val extension = if (status.fileType == "video") ".mp4" else ".jpg"
        val fileName = "status_${System.currentTimeMillis()}$extension"
        val destinationFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            val fileSize = input.available().toFloat()
            var bytesCopied = 0f

            destinationFile.outputStream().use { output ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var bytes = input.read(buffer)
                while (bytes >= 0) {
                    output.write(buffer, 0, bytes)
                    bytesCopied += bytes
                    progressCallback(bytesCopied / fileSize)
                    bytes = input.read(buffer)
                }
            }
        }

        return destinationFile
    }

    override suspend fun deleteStatus(status: StatusEntity) {
        dao.deleteStatusById(status.id)
    }

    override suspend fun saveStatus(status: StatusEntity) {
        dao.insertStatus(status)
    }

    override fun getAllSavedStatus(): Flow<List<StatusEntity>> {
        return dao.getAllStatuses()
    }
}