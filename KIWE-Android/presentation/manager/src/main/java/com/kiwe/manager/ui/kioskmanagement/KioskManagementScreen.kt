package com.kiwe.manager.ui.kioskmanagement

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.domain.model.Kiosk
import com.kiwe.manager.R
import com.kiwe.manager.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun KioskManagementScreen(kioskManagementViewModel: KioskManagementViewModel = hiltViewModel()) {
    val state by kioskManagementViewModel.collectAsState()
    val context = LocalContext.current
    kioskManagementViewModel.collectSideEffect {
        when (it) {
            is KioskManagementSideEffect.Toast -> {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            }

            KioskManagementSideEffect.ShowEditDialog -> {
//                KioskEditDialog(
//                    onDismissRequest = {
//                        //    kioskManagementViewModel.
//                    },
//                    {
//
//                    }
//                )
            }

            else -> {}
        }
    }
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(colorResource(R.color.dashboard_background)),
    ) {
        Text(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
            text = "xx커피 ㅌㅌ점",
            style = Typography.headlineLarge,
        )
        HorizontalDivider(thickness = 1.dp)
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 20.dp, horizontal = 10.dp),
        ) {
            Column {
                Text(
                    "나의 키오스크",
                )

                Card(
                    modifier =
                        Modifier
                            .weight(1F)
                            .fillMaxHeight(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = colorResource(R.color.dashboard_card_background),
                        ),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    LazyColumn {
                        state.kioskList.forEach {
                            item {
                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        it.id.toString() + "번 키오스크",
                                    )
                                    Row {
                                        Button(
                                            onClick = {
                                                kioskManagementViewModel.onKioskDelete(it.id)
                                            },
                                        ) {
                                            Text("수정")
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Button(
                                            onClick = {
                                                kioskManagementViewModel.onKioskDelete(it.id)
                                            },
                                        ) {
                                            Text("삭제")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Text(
                    "나만의 키오스크 생성",
                )

                Card(
                    modifier =
                        Modifier
                            .weight(1F)
                            .fillMaxHeight()
                            .fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = colorResource(R.color.dashboard_card_background),
                        ),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    var location by remember { mutableStateOf("") }
                    var status by remember { mutableStateOf("") }
                    OutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("지점") },
                    )

                    OutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                        value = status,
                        onValueChange = { status = it },
                        label = { Text("상태") },
                    )
                }
            }
        }
    }
}

@Composable
private fun KioskManagementScreen(kioskList: List<Kiosk>) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(colorResource(R.color.dashboard_background)),
    ) {
        Text(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
            text = "xx커피 ㅌㅌ점",
            style = Typography.headlineLarge,
        )
        HorizontalDivider(thickness = 1.dp)
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 20.dp, horizontal = 10.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "나의 키오스크",
                )

                Card(
                    modifier =
                        Modifier
                            .weight(1F)
                            .fillMaxHeight(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = colorResource(R.color.dashboard_card_background),
                        ),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    LazyColumn {
                        kioskList.forEach {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        it.id.toString() + "번 키오스크",
                                    )
                                    Row {
                                        Button(
                                            onClick = {
                                            },
                                        ) {
                                            Text("수정")
                                        }
                                        Button(
                                            onClick = {
                                            },
                                        ) {
                                            Text("삭제")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

//                Text(
//                    "나만의 키오스크 생성",
//                )

                Card(
                    modifier =
                        Modifier
                            .weight(1F)
                            .fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = colorResource(R.color.dashboard_card_background),
                        ),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    var location by remember { mutableStateOf("ssssssss") }
                    var status by remember { mutableStateOf("") }
                    OutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                        value = location,
                        singleLine = true,
                        onValueChange = { location = it },
                        label = { Text("지점") },
                    )

                    OutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                        value = status,
                        singleLine = true,
                        onValueChange = { status = it },
                        label = { Text("상태") },
                    )
                }
            }
        }
    }
}

@Preview(device = "spec: width=2304dp, height=1440dp")
@Composable
private fun KioskManagementScreenPreview() {
    KioskManagementScreen(listOf())
}
