package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEBE3D5))
    )
    Column{
        DisplayName("Kurylenko")
        Spacer(modifier = Modifier.height(70.dp))
        ImageCarousel()
        PhotoBio()
    }
}

@Composable
fun PhotoBio() {
    Box(
        modifier = Modifier
            .background(Color.LightGray, shape = RoundedCornerShape(50.dp))

    )
}

@Composable
fun Spaces() {

}

@Composable
fun DividerLine() {
    Column(
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
fun ImageCarousel() {
    Column(modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DividerLine()
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
        CustomSlider(sliderList = sliderList)
        DividerLine()
    }
}

//TODO: Make it that when you click on image, it zooms a little bit
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    sliderList: MutableList<String>,
    backwardIcon: ImageVector = Icons.Default.KeyboardArrowLeft,
    forwardIcon: ImageVector = Icons.Default.KeyboardArrowRight,
    dotsActiveColor: Color = Color.DarkGray,
    dotsInActiveColor: Color = Color.LightGray,
    dotsSize: Dp = 10.dp,
    pagerPaddingValues: PaddingValues = PaddingValues(horizontal = 65.dp),
    imageCornerRadius: Dp = 16.dp,
    imageHeight: Dp = 250.dp,
) {

    val pagerState = rememberPagerState(pageCount = {sliderList.size}, initialPage = 0) // Set the pageCount and initialPage
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier.height(120.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            IconButton(enabled = pagerState.canScrollBackward, onClick = {
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
            )  { page ->
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
                            .crossfade(true).data(sliderList[page]).build(),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.image1),
                        modifier = modifier.height(imageHeight)
                    )
                }
            }
            IconButton(enabled = pagerState.currentPage != sliderList.size - 1, onClick = {
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
                val color = if (pagerState.currentPage == it) dotsActiveColor else dotsInActiveColor
                Box(modifier = modifier
                    .padding(5.dp)
                    .clip(CircleShape)
                    .size(dotsSize)
                    .background(color)
                    .clickable {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
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