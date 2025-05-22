package com.micahnyabuto.statussaver.data.repository

import android.content.Context
import android.net.Uri
import com.micahnyabuto.statussaver.data.local.StatusDao
import com.micahnyabuto.statussaver.data.local.StatusEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import androidx.core.net.toUri

interface StatusRepository {
    suspend fun fetchStatusesFromStorage(): List<StatusEntity>
    suspend fun saveStatus(status: StatusEntity)
    fun getAllSavedStatus(): Flow<List<StatusEntity>>
}

class StatusRepositoryImpl(
    private val statusDao: StatusDao,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
): StatusRepository {
    override suspend fun fetchStatusesFromStorage(): List<StatusEntity> {
        return withContext(ioDispatcher) {
            val statuses = mutableListOf<StatusEntity>()

            val statusDirs = listOf(
                "/storage/emulated/0/WhatsApp/Media/.Statuses",
                "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/.Statuses",
                "/storage/emulated/0/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses"
            )

            for (path in statusDirs) {
                val statusDir = File(path)
                if (statusDir.exists()) {
                    statusDir.listFiles()?.forEach { file ->
                        val fileType = when {
                            file.name.endsWith(".jpg", true) || file.name.endsWith(
                                ".png",
                                true
                            ) -> "image"

                            file.name.endsWith(".mp4", true) -> "video"
                            else -> "unknown"
                        }

                        if (fileType != "unknown") {
                            statuses.add(
                                StatusEntity(
                                    filePath = file.absolutePath,
                                    fileType = fileType,
                                    dateSaved = file.lastModified()
                                )
                            )
                        }
                    }
                }
            }
            statuses
        }
    }


    override fun getAllSavedStatus(): Flow<List<StatusEntity>> {
        return statusDao.getAllStatuses()
    }

    override suspend fun saveStatus(status: StatusEntity) {
        withContext(ioDispatcher) {
            val inputUri = status.filePath.toUri()
            val inputStream = context.contentResolver.openInputStream(inputUri)

            val fileName = File(inputUri.path ?: "status").name
            val saveDir = File(context.getExternalFilesDir(null), "SavedStatuses")
            if (!saveDir.exists()) saveDir.mkdirs()

            val outputFile = File(saveDir, fileName)
            inputStream?.use { input ->
                outputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val savedEntity = status.copy(filePath = outputFile.absolutePath)
            statusDao.insertStatus(savedEntity)
        }

    }
}
