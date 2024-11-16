package com.kiwe.manager.ui.menumanagement

import android.content.Intent
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.manager.BuildConfig.BASE_IMAGE_URL
import com.kiwe.manager.R
import com.kiwe.manager.ui.component.DashBoardAnalytics
import com.kiwe.manager.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.util.Locale

@Composable
fun MenuManagementScreen(menuManagementViewModel: MenuManagementViewModel = hiltViewModel()) {
    val state by menuManagementViewModel.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    menuManagementViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MenuManagementSideEffect.Toast -> {
                Toast
                    .makeText(
                        context,
                        sideEffect.message,
                        Toast.LENGTH_SHORT,
                    ).show()
            }
            else -> {
            }
        }
    }
    if (showDialog) {
        MenuEditDialog(
            onDismissRequest = {
                showDialog = false
            },
            onConfirm = {
                menuManagementViewModel.onEditItem(it)
                showDialog = false
            },
            item = state.itemEdited,
        )
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
                DashBoardAnalytics(
                    modifier = Modifier.weight(1F),
                    buttonModifier = Modifier.width(150.dp),
                    onFirstDropdownMenuChanged = { category ->
                        menuManagementViewModel.onChangeCategory(
                            category,
                        )
                    },
                    dropDownMenuFirst =
                        listOf(
                            "전체",
                            "신상품",
                            "커피(ICE)",
                            "커피(HOT)",
                            "커피(콜드브루)",
                            "디카페인",
                            "스무디&프라페",
                            "에이드",
                            "티",
                            "음료",
                            "디저트",
                        ),
                    title = "메뉴 리스트",
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        items(state.menuList.size) {
                            MenuItem(
                                state.menuList[it],
                                onItemDelete = { menuId ->
                                    menuManagementViewModel.onDelete(menuId)
                                },
                                onItemEdit = {
                                    menuManagementViewModel.onClickEditItem(state.menuList[it])
                                    showDialog = true
                                },
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.weight(1F)) {
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
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                var text by remember { mutableStateOf("파일을 첨부해주세요!") }
                                Text(
                                    modifier = Modifier.padding(18.dp),
                                    text = "메뉴 한번에 삽입",
                                )
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    val context = LocalContext.current
                                    val launcher =
                                        rememberLauncherForActivityResult(
                                            contract = ActivityResultContracts.StartActivityForResult(),
                                        ) { result ->
                                            // 파일 선택 결과 처리
                                            val uri = result.data?.data
                                            // uri를 통해 선택된 파일 처리
                                            uri?.let {
                                                context.contentResolver
                                                    .query(
                                                        it,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                    )?.use { cursor ->
                                                        val nameIndex =
                                                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                                                        val sizeIndex =
//                                                            cursor.getColumnIndex(OpenableColumns.SIZE)
                                                        cursor.moveToFirst()

                                                        val fileName = cursor.getString(nameIndex)
                                                        text = fileName
                                                        // val fileSize = cursor.getLong(sizeIndex)
                                                    }
                                            }
                                        }
                                    Text(
                                        modifier =
                                            Modifier.clickable {
                                                val intent =
                                                    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                                        addCategory(Intent.CATEGORY_OPENABLE)
                                                        type = "*/*" // 모든 파일 타입 허용
                                                    }
                                                launcher.launch(intent)
                                            },
                                        text = text,
                                        textAlign = TextAlign.Center,
                                    )
                                    Button(
                                        modifier = Modifier.padding(18.dp),
                                        onClick = {
                                        },
                                    ) {
                                        Text("파일 첨부")
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp))
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
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    modifier = Modifier.padding(18.dp),
                                    text = "메뉴 추가",
                                )
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    item {
                                        var text by remember { mutableStateOf("") }

                                        OutlinedTextField(
                                            value = text,
                                            onValueChange = { text = it },
                                            label = { Text("카테고리") },
                                        )
                                    }

                                    item {
                                        var text by remember { mutableStateOf("") }

                                        OutlinedTextField(
                                            value = text,
                                            onValueChange = { text = it },
                                            label = { Text("Hot or Ice") },
                                        )
                                    }

                                    item {
                                        var text by remember { mutableStateOf("") }

                                        OutlinedTextField(
                                            value = text,
                                            onValueChange = { text = it },
                                            label = { Text("이름") },
                                        )
                                    }

                                    item {
                                        var text by remember { mutableStateOf("") }

                                        OutlinedTextField(
                                            value = text,
                                            onValueChange = { text = it },
                                            label = { Text("가격") },
                                        )
                                    }

                                    item {
                                        var text by remember { mutableStateOf("") }

                                        OutlinedTextField(
                                            value = text,
                                            onValueChange = { text = it },
                                            label = { Text("메뉴 요약") },
                                        )
                                    }

                                    item {
                                        var text by remember { mutableStateOf("") }

                                        OutlinedTextField(
                                            value = text,
                                            onValueChange = { text = it },
                                            label = { Text("이미지 경로") },
                                        )
                                    }
                                    item {
                                        Button(
                                            modifier = Modifier.padding(18.dp),
                                            onClick = {
                                            },
                                        ) {
                                            Text("메뉴 등록")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    item: MenuCategoryParam,
    onItemDelete: (Int) -> Unit,
    onItemEdit: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            modifier = Modifier.weight(5F),
            model = "https://" + BASE_IMAGE_URL + item.imgPath,
            contentDescription = item.description,
        )
        Text(
            modifier = Modifier.weight(1F),
            text = item.name,
        )
        Text(
            modifier = Modifier.weight(1F),
            text = String.format(Locale.getDefault(), "%,d원", item.price),
        )
        Row {
            Button(
                onClick = {
                    onItemEdit()
                },
            ) {
                Text(
                    text = "수정",
                )
            }
            Spacer(Modifier.width(10.dp))
            Button(
                onClick = { onItemDelete(item.id) },
            ) {
                Text(
                    text = "삭제",
                )
            }
        }
    }
}

@Preview(device = "spec: width=2304dp, height=1440dp")
@Composable
private fun MenuManagementScreenPreview() {
    MenuManagementScreen()
}

@Preview(device = "spec: width=2304dp, height=1440dp")
@Composable
private fun MenuManagementItemPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            modifier = Modifier.size(30.dp),
            model = "k11d205.p.ssafy.io/api/static/drinks/딸기주스.jpg",
            contentDescription = "",
        )
        Text("카페라떼")
        Text("1000원")
    }
}