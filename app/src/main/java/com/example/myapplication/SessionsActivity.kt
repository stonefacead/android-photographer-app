package com.example.myapplication

import androidx.compose.material.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class SessionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                IndexPage()
            }
        }
    }
}

@Composable
fun IndexPage() {
    val scrollableState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEBE3D5))
            .verticalScroll(state = scrollableState),
        ) {
        Box() {
            DisplayName("Kurylenko")
            BackButton()
        }
        //Spacer(modifier = Modifier.height(70.dp))
        DividerLine()
        ImageCarousel()
        DividerLine()
        UserBio(modifier = Modifier.padding(16.dp))
        DividerLine()
        Spacer(modifier = Modifier.height(16.dp))
        BookButton()
        Spacer(modifier = Modifier.height(16.dp))
        Row (
        ) {
            Contacts()
        }
    }
}

@Composable
fun BookButton() {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
        TextButton(modifier = Modifier
            .background(
                color = Color.Black//.copy(alpha = 0.3f)
                ,shape = RoundedCornerShape(16.dp)
            )
            .width(300.dp)
            .height(60.dp),
            onClick = {
                //Navigation to the payment page or whatever
            }) {
            Text(
                text = "Book a photosession",
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
                fontSize = 23.sp
            )
        }
    }
}

@Composable
fun BlogTransfer() {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
        TextButton(onClick = {
//TODO: transfer to a blog page
        },
            modifier = Modifier
                .background(Color(0xFF776B5D).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)))
        {
            Text(text = "Blog",
                color = Color.Black,
                fontFamily = FontFamily.SansSerif)
        }
    }
}

//TODO: When contacts will be known, insert them instead of just links to the main pages of the sites
@Composable
fun Contacts() {
    val context = LocalContext.current
    val intent1 = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/")) }
    val intent2 = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://web.telegram.org")) }
    val intent3 = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/")) }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val dialogOpen = remember { mutableStateOf(false) }

        TextButton(onClick = { dialogOpen.value = true },
            modifier = Modifier.background(Color(0xFF776B5D).copy(alpha = 0.3f), shape = RoundedCornerShape(16.dp))) {
            Text(text = "Contacts", color = Color.Black, fontFamily = FontFamily.SansSerif)
        }

        if (dialogOpen.value) {
            AlertDialog(
                onDismissRequest = { dialogOpen.value = false },
                buttons = {
                    Row(modifier = Modifier
                        .width(300.dp)
                        .height(100.dp)
                        .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                            Image(painter = painterResource(id = R.drawable.instagram), contentDescription = null,
                                modifier = Modifier
                                    .clickable { context.startActivity(intent1) }
                                    .size(60.dp)
                            )
                        Image(painter = painterResource(id = R.drawable.telegram), contentDescription = null,
                            modifier = Modifier
                                .clickable { context.startActivity(intent2) }
                                .size(60.dp)
                        )
                        Image(painter = painterResource(id = R.drawable.facebook), contentDescription = null,
                            modifier = Modifier
                                .clickable { context.startActivity(intent3) }
                                .size(60.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun BackButton() {
    IconButton(
        onClick = {
                  //TODO: we'll decide to what it will return later
        },
        modifier = Modifier
            .padding(6.dp)
            .background(color = Color.Transparent, shape = CircleShape)
    ) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "Return-To-MainActivity-Button")
    }
}

@Composable
private fun UserBio(modifier: Modifier) {
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val isExpandable by remember { derivedStateOf { textLayoutResult?.didOverflowHeight ?: false } }
    var isExpanded by remember { mutableStateOf(false) }
    val isButtonShown by remember { derivedStateOf { isExpandable || isExpanded } }

    // Use Box to align the Column within the container
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Adjust padding as needed
        contentAlignment = Alignment.CenterEnd // Align the content to the end (right)
    ) {
        Column(
            modifier = Modifier
                .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(16.dp))
                .width(340.dp)
        ) {
            Text(
                text = "                  Resume:",
                modifier = Modifier
                    .animateContentSize()
                    .padding(horizontal = 15.dp, vertical = 5.dp),
                maxLines = if (isExpanded) Int.MAX_VALUE else 5,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult = it },
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                modifier = Modifier
                    .animateContentSize()
                    .padding(horizontal = 15.dp, vertical = 5.dp),
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult = it },
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light
            )

            if (isButtonShown) {
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape
                ) {
                    Text(
                        text = (if (isExpanded) "Collapse" else "Expand").uppercase(),
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun DividerLine() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(width = 330.dp)
                .height(height = 3.dp)
                .background(Color.LightGray.copy(alpha = 0.5f), shape = RoundedCornerShape(30.dp))
        )
    }
}

@Composable
fun Reviews() {
    val sliderList = remember {
        mutableListOf(
            "https://www.brides.com/thmb/fJSfAbT8DxJs4dW79wcWZEQZgJs=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/must-take-wedding-photos-bride-groom-walk-clary-prfeiffer-photography-0723-primary-b4221bcb1a2b43e6b0820a8c3e3bce52.jpg",
            "https://media.istockphoto.com/id/1190043570/photo/happy-wedding-photography-of-bride-and-groom-at-wedding-ceremony-wedding-tradition-sprinkled.jpg?s=612x612&w=0&k=20&c=_aCIW5-iOIiaDdqin_50kvBcbFbIxSULHHamPUILE0c=",
            "https://b3031951.smushcdn.com/3031951/wp-content/uploads/2020/03/LA-430-scaled.jpg?lossy=0&strip=1&webp=1"
        )
    }
}

@Composable
fun ImageCarousel() {
    Column(modifier = Modifier
        .background(Color.Transparent)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .height(54.dp)
                .padding(15.dp),
            text = "Photos from previous sessions:",
            color = Color.DarkGray, // Text color

            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light
            ),
            textAlign = TextAlign.Center
        )
        val sliderList = remember {
            mutableListOf(
                "https://www.brides.com/thmb/fJSfAbT8DxJs4dW79wcWZEQZgJs=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/must-take-wedding-photos-bride-groom-walk-clary-prfeiffer-photography-0723-primary-b4221bcb1a2b43e6b0820a8c3e3bce52.jpg",
                "https://media.istockphoto.com/id/1190043570/photo/happy-wedding-photography-of-bride-and-groom-at-wedding-ceremony-wedding-tradition-sprinkled.jpg?s=612x612&w=0&k=20&c=_aCIW5-iOIiaDdqin_50kvBcbFbIxSULHHamPUILE0c=",
                "https://b3031951.smushcdn.com/3031951/wp-content/uploads/2020/03/LA-430-scaled.jpg?lossy=0&strip=1&webp=1"
            )
        }
        CustomImageSlider(sliderList = sliderList)
    }
}

//TODO: Make it that when you click on image, it zooms a little bit
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomImageSlider(
    modifier: Modifier = Modifier,
    sliderList: MutableList<String>,
    backwardIcon: ImageVector = Icons.Default.KeyboardArrowLeft,
    forwardIcon: ImageVector = Icons.Default.KeyboardArrowRight,
    dotsActiveColor: Color = Color.DarkGray,
    dotsInActiveColor: Color = Color.LightGray,
    dotsSize: Dp = 10.dp,
    pagerPaddingValues: PaddingValues = PaddingValues(horizontal = 45.dp),
    imageCornerRadius: Dp = 16.dp,
    imageHeight: Dp = 250.dp,
) {

    val pagerState = rememberPagerState(pageCount = { Int.MAX_VALUE }, initialPage = Int.MAX_VALUE / 2)
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier.height(120.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            IconButton(enabled = true, onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }) {
                Icon(imageVector = backwardIcon, contentDescription = "back")
            }
            HorizontalPager(
                state = pagerState,
                contentPadding = pagerPaddingValues,
                modifier = modifier.weight(1f)
            ) { page ->
                val adjustedPage = page % sliderList.size
                val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                val scaleFactor = 0.75f + (1f - 0.75f) * (1f - pageOffset.absoluteValue)

                Box(modifier = modifier
                    .graphicsLayer {
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    .alpha(
                        scaleFactor.coerceIn(0f, 1f)
                    )
                    .padding(10.dp)
                    .clip(RoundedCornerShape(imageCornerRadius))) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).scale(Scale.FILL)
                            .crossfade(true).data(sliderList[adjustedPage]).build(),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.image1),
                        modifier = modifier.height(imageHeight)
                    )
                }
            }
            IconButton(enabled = true, onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }) {
                Icon(imageVector = forwardIcon, contentDescription = "forward")
            }
        }
        Row(
            modifier
                .height(50.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            repeat(sliderList.size) {
                val color = if (pagerState.currentPage % sliderList.size == it) dotsActiveColor else dotsInActiveColor
                Box(modifier = modifier
                    .padding(5.dp)
                    .clip(CircleShape)
                    .size(dotsSize)
                    .background(color)
                    .clickable {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - pagerState.currentPage % sliderList.size + it)
                        }
                    })
            }
        }
    }
}




@Composable
fun DisplayName(name: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp) // Adjust the height as needed
            .background(color = Color(0xFFF3EEEA)) // Header background color
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            text = name,
            color = Color.DarkGray, // Text color

            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 55.sp,
                fontWeight = FontWeight.Light
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IndexPage()
}