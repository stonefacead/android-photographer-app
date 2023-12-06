package com.example.myapplication

import android.os.Build
import android.text.style.BackgroundColorSpan
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyRow
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenView() {
    val scrollableState = rememberScrollState()
    var selectedTags by remember { mutableStateOf(emptyList<String>()) }
    var totalPrice by remember { mutableIntStateOf(calculateTotalPrice(selectedTags)) }
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedTime by remember { mutableStateOf(LocalTime.NOON) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEBE3D5))
            .verticalScroll(state = scrollableState),
    ) {
        DisplayName("Payment")
        Spacer(modifier = Modifier.height(16.dp))
        ComposeDateTimePicker(
            pickedDate = pickedDate,
            pickedTime = pickedTime,
            onDateSelected = { pickedDate = it },
            onTimeSelected = { pickedTime = it }
        )
        Spacer(modifier = Modifier.height(8.dp))
        DividerLine()
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Additional items:",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val tagsWithPrices = listOf(
                TagPrice("Field equipment", 500),
                TagPrice("Inventory", 800),
                TagPrice("Pets", 300),
            )
            SweepableTagRow(tagsWithPrices = tagsWithPrices) { tags, _ ->
                selectedTags = tags
                totalPrice = calculateTotalPrice(selectedTags)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        DividerLine()
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Additional Description",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center)
        {//TODO: Fix this goddamn textfield
            var additionalDescription by remember { mutableStateOf("") }
            TextField(
                value = additionalDescription,
                onValueChange = { additionalDescription = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textStyle = TextStyle(fontSize = 20.sp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    placeholderColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Total:",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 30.dp)
            )
            Text(
                text = "$totalPrice hryvnas",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(vertical = 30.dp)
            )
        }
        BookButton(pickedDate = pickedDate, pickedTime = pickedTime)
    }
}

fun calculateTotalPrice(selectedTags: List<String>): Int {
    val basePrice = 3000
    val additionalPrice = selectedTags.sumBy { tag ->
        // You can adjust the prices based on your requirements
        when (tag) {
            "Field equipment" -> 500
            "Inventory" -> 800
            "Pets" -> 300
            else -> 0
        }
    }
    return basePrice + additionalPrice
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ComposeDateTimePicker(pickedDate: LocalDate,
                          pickedTime: LocalTime,
                          onDateSelected: (LocalDate) -> Unit,
                          onTimeSelected: (LocalTime) -> Unit) {
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var pickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }
    var bookedDates by remember {
        mutableStateOf(emptyList<LocalDate>())
    }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(pickedDate)
        }
    }
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm a") // Include 'a' for AM/PM
                .format(pickedTime)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(modifier = Modifier
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(16.dp)
                ),
                onClick = { dateDialogState.show() }
            ) {
                Text(
                    text = "Pick date",
                    fontSize = 25.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light
                )
            }
            Text(
                text = formattedDate,
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 29.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(modifier = Modifier
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(16.dp)
                ),
                onClick = { timeDialogState.show() }
            ) {
                Text(
                    text = "Pick time",
                    fontSize = 25.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light
                )
            }
            Text(
                text = formattedTime,
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light
            )
        }
    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                if (!bookedDates.contains(pickedDate)) {
                    bookedDates = bookedDates + pickedDate
                }
            }
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date",
            allowedDateValidator = { date ->
                date > LocalDate.now() && !bookedDates.contains(date)
            }
        ) {
            pickedDate = it
        }
    }
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                Toast.makeText(
                    context,
                    "Clicked ok",
                    Toast.LENGTH_LONG
                ).show()
            }
            negativeButton(text = "Cancel")
        }
    ) {
        timepicker(
            initialTime = LocalTime.NOON,
            title = "Pick a time",
            timeRange = LocalTime.of(6, 0)..LocalTime.of(20, 59) // Allow times from 6 AM to 8:59 PM
        ) {
            pickedTime = it
        }
    }
}

@Composable
fun BookButton(pickedDate: LocalDate, pickedTime: LocalTime) {
    val context = LocalContext.current

    TextButton(
        onClick = {
            // Perform booking action here
            Toast.makeText(
                context,
                "Booking the date $pickedDate at $pickedTime",
                Toast.LENGTH_LONG
            ).show()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(text = "Book", fontSize = 30.sp, fontWeight = FontWeight.Light, fontFamily = FontFamily.SansSerif)
    }
}

data class TagPrice(val tag: String, val price: Int)

@Composable
fun SweepableTagRow(tagsWithPrices: List<TagPrice>, onTagsSelected: (List<String>, Int) -> Unit) {
    var selectedTags by remember { mutableStateOf(emptyList<String>()) }
    var totalPrice by remember { mutableIntStateOf(0) }

    LazyRow {
        items(tagsWithPrices.size) { index ->
            val tagWithPrice = tagsWithPrices[index]
            SwappableTag(
                text = tagWithPrice.tag,
                isSelected = selectedTags.contains(tagWithPrice.tag),
                onTagClick = {
                    if (selectedTags.contains(tagWithPrice.tag)) {
                        selectedTags = selectedTags - tagWithPrice.tag
                        totalPrice -= tagWithPrice.price
                    } else {
                        selectedTags = selectedTags + tagWithPrice.tag
                        totalPrice += tagWithPrice.price
                    }
                    onTagsSelected(selectedTags, totalPrice)
                }
            )
        }
    }
}

@Composable
fun SwappableTag(text: String, isSelected: Boolean, onTagClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(if (isSelected) Color.Black else Color.Gray, RoundedCornerShape(16.dp))
            .padding(12.dp)
            .clickable { onTagClick() }
    ) {
        Text(text = text, color = if (isSelected) Color.White else Color.Black)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    ScreenView()
}
