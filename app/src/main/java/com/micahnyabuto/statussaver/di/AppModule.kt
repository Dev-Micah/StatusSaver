package com.micahnyabuto.statussaver.di

import android.content.Context
import androidx.room.Room
import com.micahnyabuto.statussaver.data.local.StatusDao
import com.micahnyabuto.statussaver.data.local.StatusDatabase
import com.micahnyabuto.statussaver.data.repository.StatusRepository
import com.micahnyabuto.statussaver.data.repository.StatusRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideStatusDatabase(@ApplicationContext context: Context): StatusDatabase{
        return Room.databaseBuilder(
            context,
            StatusDatabase::class.java,
            "status_db"
        ).build()
    }
    @Singleton
    @Provides
    fun provideStatusDao(database: StatusDatabase): StatusDao{
        return database.statusDao()
    }
    @Singleton
    @Provides
    fun provideStatusRepository(
        statusDao: StatusDao,
        @ApplicationContext context: Context

    ): StatusRepository{
        return StatusRepositoryImpl(
            statusDao = statusDao,
            context = context,
            ioDispatcher = Dispatchers.IO
        )
    }
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}