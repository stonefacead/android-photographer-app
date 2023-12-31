package com.example.myapplication

import android.content.Context.MODE_PRIVATE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterial3Api
@Composable
fun ProfileScreen(viewModel: SharedViewModel) {
    LaunchedEffect(viewModel.scheduledSessions.value) {
        viewModel.updateCompletedSessions()
    }

    val sharedPreferences = LocalContext.current.getSharedPreferences("profile_prefs", MODE_PRIVATE)
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color.Transparent
    LaunchedEffect(statusBarColor) {
        systemUiController.setStatusBarColor(statusBarColor)}

    val bookedSessions by remember { mutableStateOf(viewModel.scheduledSessions.value) }
    val finishedSessions by remember { mutableStateOf(viewModel.completedSessions.value) }

    var name by remember { mutableStateOf(sharedPreferences.getString("name", "stoneface") ?: "stoneface") }
    var description by remember { mutableStateOf(sharedPreferences.getString("description", "Some info about me. I'm testing this shit rn. I need more text. \nI'm retarded.") ?: "Some info about me. I'm testing this shit rn. I need more text. \nI'm retarded.") }

    LaunchedEffect(name, description) {
        with(sharedPreferences.edit()) {
            putString("name", name)
            putString("description", description)
            apply()
        }
    }

    val menuItems = listOf(
        MenuItem(
            title = "Completed",
            unselectedIcon = Icons.Outlined.CheckCircle,
            selectedIcon = Icons.Filled.CheckCircle
        ),
        MenuItem(
            title = "Scheduled",
            unselectedIcon = Icons.Outlined.DateRange,
            selectedIcon = Icons.Filled.DateRange
        )
    )
    Column (modifier = Modifier.fillMaxSize()) {
        var showDialog by remember { mutableStateOf(false) }

        TopBar(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(color = Color(0xFFF3EEEA)),
            onEditProfileClick = { showDialog = true }
        )

        if (showDialog) {
            EditProfileDialog(
                newName = name,
                newDescription = description,
                onNameChange = { name = it },
                onDescriptionChange = { description = it },
                onApplyClick = {
                    // Apply the changes here
                    showDialog = false // Close the dialog
                },
                onCancelClick = {
                    // Handle the cancel button click here
                    showDialog = false // Close the dialog
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        ProfileSection(name, description)

        Spacer(modifier = Modifier.height(50.dp))
        ProfileTabRow(
            menuItems = menuItems,
            bookedSessions,
            finishedSessions
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun EditProfileDialog(
    newName: String,
    newDescription: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onApplyClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelClick,
        title = { Text("Edit Profile") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextField(
                    value = newName,
                    onValueChange = { onNameChange(it) },
                    label = { Text("New Name") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = newDescription,
                    onValueChange = { onDescriptionChange(it) },
                    label = { Text("New Description") }
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancelClick) {
                    Text("Cancel")
                }
                TextButton(onClick = {
                    onApplyClick()
                    onApplyClick() // Add another call to onApplyClick to update SharedPreferences
                } ) {
                    Text("Apply")
                }
            }
        }
    )
}

@Composable
fun TopBar(
    onEditProfileClick: () -> Unit,
    modifier : Modifier = Modifier) {
    var expanded by remember{ mutableStateOf(false)}
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Text(
            text = "          My profile",
            color = Color.DarkGray,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 40.sp,
                fontWeight = FontWeight.Light
            ),
            textAlign = TextAlign.Center
        )
        IconButton(onClick = {expanded = true}, modifier = Modifier.weight(1f)) {
            Icon(Icons.Default.MoreVert, contentDescription = null, Modifier.size(40.dp))
        }
        DropdownMenu(
            expanded = expanded,
            offset = DpOffset(x = (-66).dp, y = (-10).dp),
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Edit profile") },
                onClick = {
                    expanded = false
                    onEditProfileClick() // Call the callback when edit profile is clicked
                },
                leadingIcon = {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Log out") },
                onClick = { /*edit profile menu*/ },
                leadingIcon = {
                    Icon(Icons.Default.ExitToApp, contentDescription = null)
                },
            )
        }
    }
}

@Composable
fun ProfileSection (
    name: String,
    description: String,
    modifier : Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        RoundImage(
            image = painterResource(id = R.drawable.defaultpfp),
            modifier = Modifier
                .size(100.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = name,
            color = Color.DarkGray,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            fontSize = 35.sp,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.width(30.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = description,
                color = Color.DarkGray,
                fontFamily = FontFamily.SansSerif,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
            )
        }
    }
}



@Composable
fun RoundImage(
    image: Painter,
    modifier: Modifier = Modifier
) {
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .padding(3.dp)
            .clip(CircleShape)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileTabRow(menuItems: List<MenuItem>, scheduledSessions: List<ScheduledSession>, completedSessions: List<ScheduledSession>) {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        menuItems.size
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            menuItems.forEachIndexed { index, menuItem ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = { Text(text = menuItem.title) },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedTabIndex) {
                                menuItem.selectedIcon
                            } else menuItem.unselectedIcon,
                            contentDescription = menuItem.title
                        )
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (index == 1) {
                    // This is the second tab (e.g., "Scheduled")
                    ScheduledSessions(scheduledSessions)
                } else {
                    CompletedSessions(completedSessions)
                }
            }
        }
    }
}

@Composable
fun ScheduledSessions(scheduledSessions: List<ScheduledSession>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(scheduledSessions) { session ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Date: ${session.date}, Time: ${session.time}",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun CompletedSessions(completedSessions: List<ScheduledSession>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(completedSessions) { session ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Completed - Date: ${session.date}, Time: ${session.time}",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }
}

data class ScheduledSession(val date: String, val time: String)

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@ExperimentalMaterial3Api
@Composable
fun ProfilePreview() {
    val viewModel: SharedViewModel = viewModel()
    ProfileScreen(viewModel)
}