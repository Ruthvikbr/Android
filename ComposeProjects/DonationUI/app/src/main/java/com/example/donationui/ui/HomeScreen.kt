package com.example.donationui.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donationui.R
import com.example.donationui.model.BottomMenuItem
import com.example.donationui.model.Donation
import com.example.donationui.ui.theme.*

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()

    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Greeting()
            Balance()
            Donation(
                listOf(
                    Donation("Human", R.drawable.ic_baseline_people_24),
                    Donation("Study", R.drawable.ic_baseline_menu_book_24),
                    Donation("Medicine", R.drawable.ic_baseline_medical_services_24),
                    Donation("Food", R.drawable.ic_baseline_fastfood_24),
                )
            )
            DonationProgram()
        }
        BottomMenu(items = listOf(
            BottomMenuItem("Home", R.drawable.ic_baseline_home_24),
            BottomMenuItem("Clock", R.drawable.ic_baseline_access_time_filled_24),
            BottomMenuItem("Notification", R.drawable.ic_baseline_notifications_24),
            BottomMenuItem("Profile", R.drawable.ic_baseline_person_24),
        ), modifier = Modifier.align(Alignment.BottomCenter))
    }
}


@Composable
fun Greeting(name: String = "Ruthvik") {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(mediumBlue)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.stock),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .fillMaxSize(),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop

                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "Good morning, $name",
                    style = MaterialTheme.typography.h1
                )
                Text(
                    text = "Have a good day!",
                    style = MaterialTheme.typography.body1
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(lightBlue)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun Balance() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(darkBlue)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Your Balance",
                style = MaterialTheme.typography.body1,
                color = lightBlueOverlay
            )
            Text(
                text = "$ 923",
                style = MaterialTheme.typography.h1,
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Top up",
                color = mediumBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .height(30.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .clickable {

                    }
                    .padding(vertical = 6.dp, horizontal = 25.dp)

            )
        }
    }

}

@Composable
fun Donation(donations: List<Donation>) {
    Column {
        Text(
            text = "Donation Program",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(vertical = 20.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(donations.size) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(end = 20.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(65.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(lightBlueOverlay2)
                            .clickable {

                            }

                    ) {
                        Icon(
                            painter = painterResource(id = donations[it].iconId),
                            contentDescription = donations[it].title,
                            tint = darkBlue,
                        )
                    }
                    Text(
                        text = donations[it].title,
                        style = MaterialTheme.typography.body1,
                        color = darkBlue,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DonationProgram() {
    Text(
        text = "Donation Program",
        style = MaterialTheme.typography.h1,
        modifier = Modifier.padding(vertical = 20.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(lightBlueOverlay2)
            .clip(RoundedCornerShape(10.dp))
            .padding(bottom = 100.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.children_stock),
            contentDescription = "Donation program",
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Help orphans to live happily",
            style = MaterialTheme.typography.body1,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 10.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = "Shiksha Foundation",
            style = MaterialTheme.typography.body1,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
        LinearProgressIndicator(
            progress = 0.3f,
            color = darkBlue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = "Target $ 5000",
                style = MaterialTheme.typography.body1,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )

            Text(
                text = "30%",
                style = MaterialTheme.typography.body1,
                color = Color.Black,
                fontSize = 14.sp,
            )
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun BottomMenu(
    items: List<BottomMenuItem>,
    modifier: Modifier = Modifier,
    activeHighLightColor: Color = darkBlue,
    activeTextColor: Color = darkBlue,
    inactiveTextColor: Color = mediumBlue,
    initialSelectedItemIndex: Int = 0,
) {
    var selectedItemIndex by remember {
        mutableStateOf(initialSelectedItemIndex)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(lightBlueOverlay)
            .padding(15.dp)
    ) {
        items.forEachIndexed { index, bottomMenuItem ->
            BottomMenuItemComposable(
                item = bottomMenuItem,
                isSelected = index == selectedItemIndex,
                activeHighLightColor = activeHighLightColor,
                activeTextColor = activeTextColor,
                inactiveTextColor = inactiveTextColor,
            ){
                selectedItemIndex = index
            }

        }
    }
}

@Composable
fun BottomMenuItemComposable(
    item: BottomMenuItem,
    isSelected: Boolean = false,
    activeHighLightColor: Color = darkBlue,
    activeTextColor: Color = darkBlue,
    inactiveTextColor: Color = lightBlue,
    onItemClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable {
            onItemClick()
        }
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) mediumBlue else Color.Transparent)
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(id = item.iconId),
                contentDescription = item.title,
                tint = if (isSelected) activeTextColor else inactiveTextColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

