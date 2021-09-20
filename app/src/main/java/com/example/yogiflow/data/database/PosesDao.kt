package com.example.yogiflow.data.database

import androidx.room.*
import com.example.yogiflow.data.database.entities.FavoritesEntity
import com.example.yogiflow.data.database.entities.PosesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PosesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoses(posesEntity: PosesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePose(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM poses_table ORDER BY id ASC")
    fun readPoses(): Flow<List<PosesEntity>>

    @Query("SELECT * FROM favorite_poses_table ORDER BY id ASC")
    fun readFavoritePoses(): Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavoritePose(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorite_poses_table")
    suspend fun deleteAllFavoritePoses()

}