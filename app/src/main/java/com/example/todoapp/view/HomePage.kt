package com.example.todoapp.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.model.Task
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.viewModel.TaskViewModel

enum class SubmitType(val value: String){
    ADD("Add"),
    UPDATE("Update")

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    taskViewModel: TaskViewModel = viewModel<TaskViewModel>()
) {
    val listTaskState by taskViewModel.uiState.collectAsState()
    var headerDialog by remember{ mutableStateOf("") }
    var submitType by remember { mutableStateOf(SubmitType.ADD) }
    var showDialog by remember { mutableStateOf(false) }
    var titleTextfield by remember { mutableStateOf(value = "") }
    var descriptionTexfield by remember { mutableStateOf(value = "") }
    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var taskIndexSelected by remember { mutableIntStateOf(0) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "Todo App",
                    style = MaterialTheme.typography.displayLarge
                )
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                submitType = SubmitType.ADD
                headerDialog = "Add Task"
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }

    ) {
        if (listTaskState.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Data Kosong",
                    style = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Center)
                )
            }
        }
        LazyColumn(contentPadding = it) {
            items(listTaskState.size) { index ->
                val task = listTaskState[index]
                TaskItem(task = task, onClick = {
                    headerDialog = "Update Task"
                    submitType = SubmitType.UPDATE
                    titleTextfield = task.title
                    descriptionTexfield = task.description
                    taskIndexSelected = index
                    showDialog = true

                },
                    onDelete = {
                        taskViewModel.deleteTask(index)
                    }
                    )
            }
        }

        if (showDialog) {
            DialogField(
                header = headerDialog,
                title = titleTextfield,
                errorTitle = titleError,
                buttonYesTitle =submitType.value,
                onTitleChange = { titleTextfield = it },
                description = descriptionTexfield,
                errorDescription = descriptionError,
                onDescriptionChange = { descriptionTexfield = it },
                // Dieksekusi ketika menekan layar diluar dialog
                onDismisRequest = {

                    titleError = false
                    descriptionError = false
                    showDialog = false
                    titleTextfield = ""
                    descriptionTexfield = ""
                },
                onSubmit = {
                    if(titleTextfield.isEmpty() && descriptionTexfield.isEmpty()){
                        titleError = true
                        descriptionError = true
                        return@DialogField
                    }

                    if (titleTextfield.isEmpty()) {
                        titleError = true
                        descriptionError = false
                        return@DialogField
                    }

                    if (descriptionTexfield.isEmpty()) {
                        descriptionError = true
                        titleError = false
                        return@DialogField
                    }



                    titleError = false
                    descriptionError = false
                   if(submitType == SubmitType.ADD){
                       taskViewModel.addTask(Task(title = titleTextfield, description =  descriptionTexfield))
                   }else{
                       taskViewModel.updateTask(index = taskIndexSelected,task = Task(title = titleTextfield, description =  descriptionTexfield))
                   }
                    showDialog = false
                    titleTextfield = ""
                    descriptionTexfield = ""

                },
                onCancel = {

                    titleError = false
                    descriptionError = false
                    showDialog = false
                    titleTextfield = ""
                    descriptionTexfield = ""
                }
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(modifier: Modifier = Modifier, task: Task, onClick : () -> Unit, onDelete : () -> Unit) {
    Card(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.titleLarge)
                Text(text = task.description, style = MaterialTheme.typography.bodySmall)

            }
            Divider(modifier = Modifier.width(15.dp))
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogField(
    modifier: Modifier = Modifier,
    header: String,
    title: String,
    buttonYesTitle: String,
    errorTitle: Boolean,
    onTitleChange: (String) -> Unit,
    description: String,
    errorDescription: Boolean,
    onDescriptionChange: (String) -> Unit,
    onDismisRequest: () -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit
) {


    Dialog(
        onDismissRequest = onDismisRequest
    ) {
        // Focus manager harus di dalam dialog
        val focusManager = LocalFocusManager.current
        Box(
            modifier = modifier
                .clip(shape = RoundedCornerShape(12))
                .background(Color.White)
                .padding(horizontal = 25.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(text = header,  style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.Black
                ))
                Divider(
                    modifier = Modifier.height(20.dp),
                    color = Color.White
                )
                OutlinedTextField(
                    value = title,
                    isError = errorTitle,
                    onValueChange = onTitleChange,
                    shape = RoundedCornerShape(5.dp),
                    label = {
                        Text(text = "Title")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(

                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black

                    )
                )
                Divider(
                    modifier = Modifier.height(15.dp),
                    color = Color.White
                )
                OutlinedTextField(
                    value = description,
                    isError = errorDescription,
                    onValueChange = onDescriptionChange,
                    shape = RoundedCornerShape(5.dp),
                    label = {
                        Text(text = "Description")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {

                            focusManager.clearFocus()
                        }
                    ),
                    minLines = 3,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black

                    )


                )
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ElevatedButton(onClick = onSubmit, shape = RoundedCornerShape(12.dp)) {
                        Text(text = buttonYesTitle)
                    }
                    Divider(
                        color = Color.White,
                        modifier = Modifier.width(10.dp)
                    )
                    ElevatedButton(onClick = onCancel, shape = RoundedCornerShape(12.dp)) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoAppTheme {
        HomePage()
    }
}