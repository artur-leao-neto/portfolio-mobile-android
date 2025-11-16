package com.example.cartaovisitaartur.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY id ASC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(projects: List<ProjectEntity>)

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: Int): Flow<ProjectEntity?>
}


