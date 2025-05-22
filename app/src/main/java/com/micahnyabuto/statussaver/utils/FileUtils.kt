package com.micahnyabuto.statussaver.utils

import android.content.Context
import android.os.Environment
import java.io.File

object FileUtils {

    fun saveStatusToInternalStorage(
        context: Context,
        sourceFile: File,
        fileType: String
    ): File? {
        try {
            val targetDir = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "StatusSaver"
            )
            if (!targetDir.exists()) targetDir.mkdirs()

            val destFile = File(targetDir, sourceFile.name)
            sourceFile.copyTo(destFile, overwrite = true)
            return destFile
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
