package com.micahnyabuto.statussaver.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.micahnyabuto.statussaver.data.local.StatusDao
import com.micahnyabuto.statussaver.data.local.StatusEntity
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

interface StatusRepository {
    suspend fun fetchStatusesFromStorage(): List<StatusEntity>
    suspend fun saveStatus(status: StatusEntity)
    fun getAllSavedStatus(): Flow<List<StatusEntity>>
    fun saveSafUri(uri: Uri)
}

class StatusRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dao: StatusDao
) : StatusRepository {

    override suspend fun fetchStatusesFromStorage(): List<StatusEntity> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            fetchStatusesFromSAF()
        } else {
            fetchStatusesFromLegacyStorage()
        }
    }

    private fun fetchStatusesFromLegacyStorage(): List<StatusEntity> {
        val statusDir = File(
            Environment.getExternalStorageDirectory(),
            "WhatsApp/Media/.Statuses"
        )

        if (!statusDir.exists() || !statusDir.isDirectory) return emptyList()

        return statusDir.listFiles()
            ?.filter { it.name.endsWith(".jpg") || it.name.endsWith(".mp4") }
            ?.map { file ->
                StatusEntity(
                    filePath = file.absolutePath,
                    fileType = if (file.name.endsWith(".mp4")) "video" else "image",
                    dateSaved = file.lastModified()
                )
            } ?: emptyList()
    }

    private fun fetchStatusesFromSAF(): List<StatusEntity> {
        val prefs = context.getSharedPreferences("status_prefs", Context.MODE_PRIVATE)
        val uriString = prefs.getString("saf_uri", null) ?: return emptyList()
        val safUri = uriString.toUri()
        val documentFile = DocumentFile.fromTreeUri(context, safUri) ?: return emptyList()

        return documentFile.listFiles()
            .filter { it.name?.endsWith(".jpg") == true || it.name?.endsWith(".mp4") == true }
            .mapNotNull { file ->
                StatusEntity(
                    filePath = file.uri.toString(),
                    fileType = if (file.name?.endsWith(".mp4") == true) "video" else "image",
                    dateSaved = file.lastModified()
                )
            }
    }

    override suspend fun saveStatus(status: StatusEntity) {
        dao.insertStatus(status)
    }

    override fun getAllSavedStatus(): Flow<List<StatusEntity>> {
        return dao.getAllStatuses()
    }

    override fun saveSafUri(uri: Uri) {
        context.contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        context.getSharedPreferences("status_prefs", Context.MODE_PRIVATE).edit {
            putString("saf_uri", uri.toString())
        }
    }
}
