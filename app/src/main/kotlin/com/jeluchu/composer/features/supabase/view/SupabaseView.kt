package com.jeluchu.composer.features.supabase.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.BuildConfig
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import com.jeluchu.jchucomponents.supabase.JchuSupabase
import com.jeluchu.jchucomponents.supabase.JchuSupabaseAuthConfig
import com.jeluchu.jchucomponents.supabase.JchuSupabaseConfig
import com.jeluchu.jchucomponents.supabase.createJchuSupabaseClient
import com.jeluchu.jchucomponents.supabase.database.JchuSupabaseTable
import com.jeluchu.jchucomponents.supabase.database.create
import com.jeluchu.jchucomponents.supabase.database.deleteById
import com.jeluchu.jchucomponents.supabase.database.findAll
import com.jeluchu.jchucomponents.supabase.database.updateById
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Composable
fun SupabaseView(onBack: () -> Unit) {
    SupabaseCatalog(onBack = onBack)
}

@Composable
private fun SupabaseCatalog(onBack: () -> Unit) {
    val authRedirectUrl = "com.jeluchu.composer://supabase-auth"
    val configReady =
        BuildConfig.SUPABASE_URL.isNotBlank() &&
            BuildConfig.SUPABASE_PUBLISHABLE_KEY.isNotBlank()
    val supabase =
        remember(configReady) {
            if (configReady) {
                createJchuSupabaseClient(
                    JchuSupabaseConfig(
                        url = BuildConfig.SUPABASE_URL,
                        publishableKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY,
                        auth =
                            JchuSupabaseAuthConfig(
                                autoLoadFromStorage = false,
                                defaultRedirectUrl = authRedirectUrl,
                                scheme = "com.jeluchu.composer",
                                host = "supabase-auth"
                            )
                    )
                )
            } else {
                null
            }
        }
    val notesTable = remember(supabase) { supabase?.database?.table(SupabaseNoteTable) }
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("Nota desde JchuComponents") }
    var status by remember {
        mutableStateOf(
            if (configReady) {
                "Config ready"
            } else {
                "Missing Supabase config in local.properties"
            }
        )
    }
    var isLoading by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(emptyList<SupabaseNote>()) }

    fun loadNotes() {
        val table = notesTable ?: return
        coroutineScope.collectResource(
            flow = table.findAll(),
            onLoading = {
                isLoading = true
                status = "Loading notes"
            },
            onSuccess = { loadedNotes ->
                isLoading = false
                notes = loadedNotes.sortedByDescending { it.createdAt }
                status = "Loaded ${loadedNotes.size} notes"
            },
            onError = { failure ->
                isLoading = false
                status = failure.message
            }
        )
    }

    fun signIn() {
        val client = supabase ?: return
        coroutineScope.collectResource(
            flow = client.auth.signInWithEmail(email, password),
            onLoading = {
                isLoading = true
                status = "Signing in"
            },
            onSuccess = {
                isLoading = false
                status = "Signed in"
            },
            onError = { failure ->
                isLoading = false
                status = failure.message
            }
        )
    }

    fun signUp() {
        val client = supabase ?: return
        coroutineScope.collectResource(
            flow = client.auth.signUpWithEmail(
                email = email,
                password = password,
                redirectUrl = authRedirectUrl
            ),
            onLoading = {
                isLoading = true
                status = "Signing up"
            },
            onSuccess = {
                isLoading = false
                status = "Signed up. Check email confirmation if enabled."
            },
            onError = { failure ->
                isLoading = false
                status = failure.message
            }
        )
    }

    fun signOut() {
        val client = supabase ?: return
        coroutineScope.collectResource(
            flow = client.auth.signOut(),
            onLoading = {
                isLoading = true
                status = "Signing out"
            },
            onSuccess = {
                isLoading = false
                notes = emptyList()
                status = "Signed out"
            },
            onError = { failure ->
                isLoading = false
                status = failure.message
            }
        )
    }

    fun addNote() {
        val table = notesTable ?: return
        val nextNote =
            SupabaseNote(
                id = "note-${System.currentTimeMillis()}",
                title = title.ifBlank { "Untitled note" },
                completed = false
            )
        coroutineScope.collectResource(
            flow = table.create(nextNote),
            onLoading = {
                isLoading = true
                status = "Adding note"
            },
            onSuccess = {
                isLoading = false
                status = "Note added"
                loadNotes()
            },
            onError = { failure ->
                isLoading = false
                status = failure.message
            }
        )
    }

    fun toggleNote(note: SupabaseNote) {
        val table = notesTable ?: return
        coroutineScope.collectResource(
            flow = table.updateById(
                id = note.id,
                value = note.copy(completed = !note.completed)
            ),
            onLoading = {
                isLoading = true
                status = "Updating note"
            },
            onSuccess = {
                isLoading = false
                status = "Note updated"
                loadNotes()
            },
            onError = { failure ->
                isLoading = false
                status = failure.message
            }
        )
    }

    fun deleteNote(note: SupabaseNote) {
        val table = notesTable ?: return
        coroutineScope.collectResource(
            flow = table.deleteById(note.id),
            onLoading = {
                isLoading = true
                status = "Deleting note"
            },
            onSuccess = {
                isLoading = false
                status = "Note deleted"
                loadNotes()
            },
            onError = { failure ->
                isLoading = false
                status = failure.message
            }
        )
    }

    SupabaseContent(
        onBack = onBack,
        configReady = configReady,
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        title = title,
        onTitleChange = { title = it },
        status = status,
        isLoading = isLoading,
        notes = notes,
        onSignIn = ::signIn,
        onSignUp = ::signUp,
        onSignOut = ::signOut,
        onLoadNotes = ::loadNotes,
        onAddNote = ::addNote,
        onToggleNote = ::toggleNote,
        onDeleteNote = ::deleteNote
    )
}

@Composable
private fun SupabaseContent(
    onBack: () -> Unit,
    configReady: Boolean,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    status: String,
    isLoading: Boolean,
    notes: List<SupabaseNote>,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    onSignOut: () -> Unit,
    onLoadNotes: () -> Unit,
    onAddNote: () -> Unit,
    onToggleNote: (SupabaseNote) -> Unit,
    onDeleteNote: (SupabaseNote) -> Unit
) {
    ScaffoldStructure(
        title = Names.supabase,
        onNavIconClick = onBack
    ) {
        SupabaseSection("Connection") {
            Text(
                text = if (configReady) BuildConfig.SUPABASE_URL else "No local Supabase config",
                style = JchuCatalogTheme.typography.body,
                color = JchuCatalogTheme.colors.content
            )
            Text(
                text = if (isLoading) "Working..." else status,
                style = JchuCatalogTheme.typography.body,
                color = JchuCatalogTheme.colors.content.copy(alpha = .72f)
            )
        }

        SupabaseSection("Auth") {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                singleLine = true,
                enabled = configReady && !isLoading
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                singleLine = true,
                enabled = configReady && !isLoading,
                visualTransformation = PasswordVisualTransformation()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen08)) {
                Button(
                    enabled = configReady && !isLoading && email.isNotBlank() && password.isNotBlank(),
                    onClick = onSignIn
                ) {
                    Text("Sign in")
                }
                Button(
                    enabled = configReady && !isLoading && email.isNotBlank() && password.isNotBlank(),
                    onClick = onSignUp
                ) {
                    Text("Sign up")
                }
                Button(
                    enabled = configReady && !isLoading,
                    onClick = onSignOut
                ) {
                    Text("Sign out")
                }
            }
        }

        SupabaseSection("Database") {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = onTitleChange,
                label = { Text("New note title") },
                singleLine = true,
                enabled = configReady && !isLoading
            )
            Row(horizontalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen08)) {
                Button(
                    enabled = configReady && !isLoading,
                    onClick = onLoadNotes
                ) {
                    Text("Load")
                }
                Button(
                    enabled = configReady && !isLoading,
                    onClick = onAddNote
                ) {
                    Text("Add")
                }
            }
        }

        SupabaseSection("Notes") {
            if (notes.isEmpty()) {
                Text(
                    text = "No notes loaded",
                    style = JchuCatalogTheme.typography.body,
                    color = JchuCatalogTheme.colors.content.copy(alpha = .72f)
                )
            } else {
                notes.forEach { note ->
                    SupabaseNoteRow(
                        note = note,
                        enabled = configReady && !isLoading,
                        onToggle = { onToggleNote(note) },
                        onDelete = { onDeleteNote(note) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SupabaseSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(JchuCatalogTheme.colors.background)
                .padding(JchuCatalogTheme.spacing.dimen12),
        verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen08)
    ) {
        Text(
            text = title,
            style = JchuCatalogTheme.typography.section,
            color = JchuCatalogTheme.colors.content,
            fontWeight = FontWeight.Bold
        )
        content()
    }
}

@Composable
private fun SupabaseNoteRow(
    note: SupabaseNote,
    enabled: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(JchuCatalogTheme.colors.surface)
                .padding(JchuCatalogTheme.spacing.dimen12),
        verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen08)
    ) {
        Text(
            text = note.title,
            style = JchuCatalogTheme.typography.body,
            color = JchuCatalogTheme.colors.content,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (note.completed) "Completed" else "Pending",
            style = JchuCatalogTheme.typography.body,
            color = JchuCatalogTheme.colors.content.copy(alpha = .72f)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen08)) {
            Button(
                enabled = enabled,
                onClick = onToggle
            ) {
                Text(if (note.completed) "Undo" else "Done")
            }
            Button(
                enabled = enabled,
                onClick = onDelete
            ) {
                Text("Delete")
            }
        }
    }
}

private fun <T> CoroutineScope.collectResource(
    flow: Flow<Resource<Failure, T>>,
    onLoading: () -> Unit,
    onSuccess: (T) -> Unit,
    onError: (Failure) -> Unit
) {
    launch {
        flow.collect { resource ->
            when (resource) {
                is Resource.Loading -> onLoading()
                is Resource.Success -> resource.data?.let(onSuccess)
                is Resource.Error -> resource.error?.let(onError)
            }
        }
    }
}

@Serializable
private data class SupabaseNote(
    val id: String,
    val title: String,
    val completed: Boolean = false,
    @SerialName("created_at")
    val createdAt: String? = null
)

private object SupabaseNoteTable : JchuSupabaseTable<SupabaseNote> {
    override val name: String = "jchu_supabase_notes"
}

@Preview(name = "Supabase - Light", showBackground = true)
@Composable
private fun SupabaseLightPreview() {
    JeluchuTheme {
        SupabaseContent(
            onBack = {},
            configReady = true,
            email = "test@example.com",
            onEmailChange = {},
            password = "password",
            onPasswordChange = {},
            title = "Nota desde preview",
            onTitleChange = {},
            status = "Preview",
            isLoading = false,
            notes =
                listOf(
                    SupabaseNote(
                        id = "preview",
                        title = "Validar PostgREST",
                        completed = false
                    )
                ),
            onSignIn = {},
            onSignUp = {},
            onSignOut = {},
            onLoadNotes = {},
            onAddNote = {},
            onToggleNote = {},
            onDeleteNote = {}
        )
    }
}

@Preview(
    name = "Supabase - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SupabaseDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            SupabaseContent(
                onBack = {},
                configReady = true,
                email = "test@example.com",
                onEmailChange = {},
                password = "password",
                onPasswordChange = {},
                title = "Nota desde preview",
                onTitleChange = {},
                status = "Preview",
                isLoading = false,
                notes = emptyList(),
                onSignIn = {},
                onSignUp = {},
                onSignOut = {},
                onLoadNotes = {},
                onAddNote = {},
                onToggleNote = {},
                onDeleteNote = {}
            )
        }
    }
}
