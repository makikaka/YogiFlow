package com.example.yogiflow.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.yogiflow.data.database.entities.FavoritesEntity
import com.example.yogiflow.data.database.entities.PosesEntity

@Database(
    entities = [PosesEntity::class, FavoritesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PosesTypeConverter::class)
abstract class PosesDatabase: RoomDatabase() {

    abstract fun posesDao(): PosesDao

}