package com.klen0010.flinders.zootreasurehunt.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.klen0010.flinders.zootreasurehunt.R
import com.klen0010.flinders.zootreasurehunt.model.Sighting
import com.klen0010.flinders.zootreasurehunt.utils.FileUtils

// This pop-up lets you add notes, check a 'found' box, or snap a photo of the animal
@Composable
fun EditSightingDialog(sighting: Sighting, onDismiss: () -> Unit, onSave: (Sighting) -> Unit) {
    var notesText by remember { mutableStateOf(sighting.notes) }
    var isFoundChecked by remember { mutableStateOf(sighting.isFound) }
    val context = LocalContext.current
    val fileUtils = remember { FileUtils(context) }
    var currentPhotoPath by remember { mutableStateOf(sighting.photoPath) }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Logic to check if the input is valid (notes shouldn't be empty)
    val isNotesEmpty = notesText.isBlank()

    // Handles the camera magic for taking a picture
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            currentPhotoPath = tempPhotoUri.toString()
        }
    }

    // The actual pop-up box
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.edit_animal)) },
        text = {
            Column {
                // Input field with validation error support
                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text(stringResource(id = R.string.notes_hint)) },
                    isError = isNotesEmpty,
                    supportingText = {
                        if (isNotesEmpty) {
                            Text(text = stringResource(id = R.string.notes_error))
                        }
                    }
                )
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isFoundChecked,
                        onCheckedChange = { isFoundChecked = it }
                    )
                    Text(text = stringResource(id = R.string.checkbox_found))
                }
                // Button to fire up the camera
                Button(
                    onClick = {
                        val file = fileUtils.createImageFile()
                        val uri = fileUtils.getUriForFile(file)
                        tempPhotoUri = uri
                        uri?.let { cameraLauncher.launch(it) }
                    }) {
                    Text(text = if (currentPhotoPath == null) "Take Photo" else "Retake Photo")
                }
            }
        },
        confirmButton = {
            Button(
                // Only let the user save if they've written some notes
                enabled = !isNotesEmpty,
                onClick = {
                    onSave(sighting.copy(
                        isFound = isFoundChecked,
                        notes = notesText,
                        photoPath = currentPhotoPath
                    ))
                }
            ) {
                Text(text = stringResource(id = R.string.save_btn))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel_btn))
            }
        }
    )
}