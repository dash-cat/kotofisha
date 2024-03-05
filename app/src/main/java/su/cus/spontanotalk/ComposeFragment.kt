package su.cus.spontanotalk


import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import java.util.*

class ComposeFragment : Fragment() {
    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View? {
        return ComposeView(requireContext()).apply {
            setContent {
                var timeRecords by remember { mutableStateOf(listOf(
                    TimeRecord("Design Meeting", "2 hours"),
                    TimeRecord("Development", "5 hours"),
                    TimeRecord("Testing", "1 hour")
                )) }

                TimeTable(timeRecords, onAddRecord = { task, time ->
                    timeRecords = timeRecords + TimeRecord(task, time)
                })
            }
        }
    }
}

@Composable
fun TimeTable(timeRecords: List<TimeRecord>, onAddRecord: (String, String) -> Unit) {
    var task by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Time Tracking",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        TextField(value = task, onValueChange = { task = it }, label = { Text("Task") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = time, onValueChange = { time = it }, label = { Text("Time Spent") })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if (task.isNotEmpty() && time.isNotEmpty()) {
                onAddRecord(task, time)
                task = ""
                time = ""
            }
        }) {
            Text("Add Record")
        }

        LazyColumn {
            items(timeRecords) { record ->
                TimeRow(record)
            }
        }
    }
}

@Composable
fun TimeRow(record: TimeRecord) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = record.taskName,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        Text(text = record.timeSpent)
    }
}

@Preview
@Composable
fun PreviewTimeTable() {
    TimeTable(
        listOf(
            TimeRecord("Design Meeting", "2 hours"),
            TimeRecord("Development", "5 hours"),
            TimeRecord("Testing", "1 hour")
        ), onAddRecord = { _, _ -> }
    )
}

data class TimeRecord(val taskName: String, val timeSpent: String)

@Composable
fun TaskEditor() {
    var taskName by remember { mutableStateOf("") }
    var taskDate by remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf("") }

    Column {
        TextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text("Название задачи") }
        )

        Text(text = "Выбранная дата: ${selectedDate.value}")

        Button(onClick = {
//            TaskDatePicker(selectedDate)
        }) {
            Text("Выбрать дату")
        }

        // Добавьте логику для сохранения задачи с названием и выбранной датой
    }
}


@Composable
fun TaskDatePicker(selectedDate: MutableState<String>) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    LaunchedEffect(key1 = true) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                // Обновляем состояние выбранной даты
                selectedDate.value = "$dayOfMonth/${month + 1}/$year"
            }, year, month, day
        )
        datePickerDialog.show()
    }
}