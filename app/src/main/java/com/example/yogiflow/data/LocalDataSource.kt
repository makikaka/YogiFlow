package com.example.yogiflow.data

import com.example.yogiflow.data.database.PosesDao
import com.example.yogiflow.data.database.entities.FavoritesEntity
import com.example.yogiflow.data.database.entities.PosesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val posesDao: PosesDao
) {

    fun readPoses(): Flow<List<PosesEntity>> {
        return posesDao.readPoses()
    }

    fun readFavoritePoses(): Flow<List<FavoritesEntity>> {
        return posesDao.readFavoritePoses()
    }

    suspend fun insertPoses(posesEntity: PosesEntity) {
        posesDao.insertPoses(posesEntity)
    }

    suspend fun insertFavoritePoses(favoritesEntity: FavoritesEntity) {
        posesDao.insertFavoritePose(favoritesEntity)
    }

    suspend fun deleteFavoritePose(favoritesEntity: FavoritesEntity) {
        posesDao.deleteFavoritePose(favoritesEntity)
    }

    suspend fun deleteAllFavoritePoses() {
        posesDao.deleteAllFavoritePoses()
    }

}