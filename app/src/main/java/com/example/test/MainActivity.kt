package com.example.test

import android.os.Bundle
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.test.ui.theme.TestTheme

class MarvelHero(name: String, message: String, image: String)
{
    private var _name : String = name
    private var _message : String = message
    private var _image : String = image

    fun getName(): String {
        return _name
    }

    fun getMessage(): String {
        return _message
    }

    fun getImage(): String {
        return _image
    }
}

var listOfHeroes = listOf(
    MarvelHero(
        name = "Iron Man",
        message = "I AM IRON MAN",
        image = "https://iili.io/JMnuDI2.png"
    ),

    MarvelHero(
        name = "Deadpool",
        message = "Hi, it's me - Deadpool!",
        image = "https://iili.io/JMnAfIV.png"
    ),

    MarvelHero(
        name = "Spider Man",
        message = "In iron suit",
        image = "https://iili.io/JMnuyB9.png"
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val listState: LazyListState = rememberLazyListState()

            TestTheme {
                NavHost(navController = navController,
                    startDestination = "MainScreen"
                ) {
                    composable(route = "MainScreen"
                    ){
                        MainScreen(navController = navController, listState = listState)
                    }

                    composable(route = "HeroScreen_{index}",
                        arguments = listOf(
                            navArgument("index"){
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry -> HeroScreen(
                            index =  backStackEntry.arguments?.getInt("index"),
                            heroesList = listOfHeroes,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ListRow(model: MarvelHero, navController: NavController) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = {
                navController.navigate(route = "HeroScreen_${listOfHeroes.indexOf(model)}",
                )
            })
    ) {
        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(model.getImage())
                    .crossfade(true)
                    .build(),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 70.dp, start = 30.dp, end = 30.dp)
                    .size(width = 300.dp, height = 550.dp)

            )

            Text(
                text = model.getName(),
                fontSize = 40.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(top = 570.dp, start = 33.dp, end = 0.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavController, listState: LazyListState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Black,
                        Color.DarkGray,
                        Color.Red
                    ), start = Offset(666.0f, 666.0f)
                ),
                shape = RoundedCornerShape(2.dp)
            )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://iili.io/JMnuvbp.png")
                .crossfade(true)
                .build(),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 70.dp, start = 0.dp, end = 0.dp)
        )

        Text(
            text = "Choose your Hero",
            fontSize = 35.sp,
            color = Color.White,
            modifier = Modifier
                .padding(top = 60.dp)
        )



        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                state = listState,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
                modifier = Modifier
                    .fillMaxSize()
                ) {
                items(listOfHeroes) { hero ->
                    ListRow(model = hero, navController = navController)
                }
            }
        }
    }
}


@Composable
fun HeroScreen(index: Int?, heroesList: List<MarvelHero>, navController: NavController) {

    val hero = heroesList.elementAt(index!!)
    Box(modifier = Modifier
        .fillMaxSize()
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(hero.getImage())
                .crossfade(true)
                .build(),
            contentDescription = hero.getName(),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )

        Text(
            text = hero.getName(),
            fontSize = 45.sp,
            color = Color.White,
            modifier = Modifier
                .padding(top = 750.dp, start = 32.dp, end = 0.dp)

        )

        Text(
            text = hero.getMessage(),
            fontSize = 35.sp,
            color = Color.White,
            modifier = Modifier
                .padding(top = 810.dp, start = 32.dp, end = 0.dp)
        )

        Box(
            modifier = Modifier
                .padding(top = 23.dp, start = 2.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "Arrow",
                modifier = Modifier
                    .padding(top = 20.dp, start = 10.dp)
                    .clickable { navController.navigate("MainScreen") }
                    .size(43.dp)
            )
        }
    }
}

