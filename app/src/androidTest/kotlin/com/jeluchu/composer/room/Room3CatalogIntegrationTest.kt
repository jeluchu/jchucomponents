package com.jeluchu.composer.room

import android.content.Context
import androidx.room3.Dao
import androidx.room3.Database
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jeluchu.jchucomponents.room.configureJchuRoom
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class Room3CatalogIntegrationTest {
    private lateinit var database: Room3CatalogDatabase

    @After
    fun closeDatabase() {
        if (::database.isInitialized) {
            database.close()
        }
    }

    @Test
    fun configuredBuilderPersistsDataWithTheBundledDriver() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder<Room3CatalogDatabase>(context)
                .configureJchuRoom()
                .build()

        database.itemDao().insert(Room3CatalogItem(id = 1, label = "Room 3"))

        assertEquals(1, database.itemDao().count())
    }
}

@Database(entities = [Room3CatalogItem::class], version = 1, exportSchema = false)
abstract class Room3CatalogDatabase : RoomDatabase() {
    abstract fun itemDao(): Room3CatalogItemDao
}

@Dao
interface Room3CatalogItemDao {
    @Insert
    suspend fun insert(item: Room3CatalogItem)

    @Query("SELECT COUNT(*) FROM catalog_room_item")
    suspend fun count(): Int
}

@androidx.room3.Entity(tableName = "catalog_room_item")
data class Room3CatalogItem(
    @androidx.room3.PrimaryKey val id: Int,
    val label: String
)
