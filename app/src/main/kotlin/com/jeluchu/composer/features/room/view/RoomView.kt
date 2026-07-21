package com.jeluchu.composer.features.room.view

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.room3.Dao
import androidx.room3.Database
import androidx.room3.Delete
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.Update
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.room.configureJchuRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun RoomView(onBack: () -> Unit) {
    val context = LocalContext.current.applicationContext
    val database = remember(context) { createRoomCatalogDatabase(context) }
    val notes by database.noteDao().observeAll().collectAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    var editingNote by remember { mutableStateOf<RoomCatalogNote?>(null) }

    DisposableEffect(database) {
        onDispose(database::close)
    }

    ScaffoldStructure(title = Names.room, onNavIconClick = onBack) {
        Text("Añade una nota, edítala o elimínala para probar Room 3 con BundledSQLiteDriver.")
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nota") },
            singleLine = true
        )
        Button(
            enabled = text.isNotBlank(),
            onClick = {
                coroutineScope.launch {
                    val note = editingNote
                    if (note == null) {
                        database.noteDao().insert(RoomCatalogNote(text = text.trim()))
                    } else {
                        database.noteDao().update(note.copy(text = text.trim()))
                    }
                    text = ""
                    editingNote = null
                }
            }
        ) {
            Text(if (editingNote == null) "Añadir" else "Guardar cambios")
        }

        notes.forEach { note ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(note.text)
                    Text(if (note.completed) "Actualizada" else "Pendiente")
                }
                Button(
                    onClick = {
                        editingNote = note
                        text = note.text
                    }
                ) {
                    Text("Editar")
                }
                Button(
                    onClick = {
                        coroutineScope.launch { database.noteDao().delete(note) }
                    }
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

private fun createRoomCatalogDatabase(context: Context): RoomCatalogDatabase =
    Room.databaseBuilder<RoomCatalogDatabase>(context, "jchucomponents-room-catalog.db")
        .configureJchuRoom()
        .build()

@Database(entities = [RoomCatalogNote::class], version = 1, exportSchema = false)
abstract class RoomCatalogDatabase : RoomDatabase() {
    abstract fun noteDao(): RoomCatalogNoteDao
}

@Dao
interface RoomCatalogNoteDao {
    @Query("SELECT * FROM room_catalog_notes ORDER BY id DESC")
    fun observeAll(): Flow<List<RoomCatalogNote>>

    @Insert
    suspend fun insert(note: RoomCatalogNote)

    @Update
    suspend fun update(note: RoomCatalogNote)

    @Delete
    suspend fun delete(note: RoomCatalogNote)
}

@Entity(tableName = "room_catalog_notes")
data class RoomCatalogNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val completed: Boolean = false
)
