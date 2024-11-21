//package com.kiwe.manager.ui.kioskmanagement
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Dialog
//import androidx.compose.ui.window.DialogProperties
//import com.kiwe.domain.model.Kiosk
//import com.kiwe.domain.model.MenuCategoryParam
//import com.kiwe.domain.model.MenuParam
//import com.kiwe.manager.ui.menumanagement.MenuEditDialog
//
//@Composable
//fun KioskEditDialog(
//    onDismissRequest: () -> Unit,
//    onConfirm: () -> Unit,
//    item: Kiosk,
//) {
//
//    Dialog(
//        onDismissRequest = onDismissRequest,
//        properties =
//            DialogProperties(
//                dismissOnBackPress = false, // 뒤로가기 눌러도 닫히지 않음
//                dismissOnClickOutside = false, // 외부 클릭으로 닫히지 않음
//            ),
//    ) {
//        Column(
//            modifier =
//                Modifier
//                    .background(
//                        Color.White,
//                    ).padding(10.dp),
//        ) {
//            OutlinedTextField(
//                modifier = Modifier.width(250.dp),
//                value = category,
//                singleLine = true,
//                onValueChange = { category = it },
//                label = { Text("카테고리") },
//            )
//
//            OutlinedTextField(
//                modifier = Modifier.width(250.dp),
//                value = hotOrIce,
//                singleLine = true,
//                onValueChange = { hotOrIce = it },
//                label = { Text("HotOrIce") },
//            )
//            OutlinedTextField(
//                modifier = Modifier.width(250.dp),
//                value = name,
//                singleLine = true,
//                onValueChange = { name = it },
//                label = { Text("이름") },
//            )
//            OutlinedTextField(
//                modifier = Modifier.width(250.dp),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                singleLine = true,
//                value = price.toString(),
//                onValueChange = { price = it.toInt() },
//                label = { Text("가격") },
//            )
//            OutlinedTextField(
//                modifier = Modifier.width(250.dp),
//                value = description,
//                singleLine = true,
//                onValueChange = { description = it },
//                label = { Text("메뉴 요약") },
//            )
//            OutlinedTextField(
//                modifier = Modifier.width(250.dp),
//                value = imgPath,
//                singleLine = true,
//                onValueChange = { imgPath = it },
//                label = { Text("이미지 경로") },
//            )
//
//            Row(
//                modifier = Modifier.width(250.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//            ) {
//                Button(
//                    onClick = {
//                        onConfirm(
//                            MenuParam(
//                                category = category,
//                                hotOrIce = hotOrIce,
//                                name = name,
//                                price = price,
//                                description = description,
//                                imgPath = imgPath,
//                            ),
//                        )
//                    },
//                ) {
//                    Text("수정하기")
//                }
//                Button(
//                    onClick = {
//                        onDismissRequest()
//                    },
//                ) {
//                    Text("취소")
//                }
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun MenuEditDialogPreview() {
//    MenuEditDialog(
//        onDismissRequest = {},
//        onConfirm = {},
//        item =
//            MenuCategoryParam(
//                id = 6021,
//                category = "sit",
//                categoryNumber = 4529,
//                hotOrIce = "pertinax",
//                name = "Melissa Woods",
//                price = 3911,
//                description = "praesent",
//                imgPath = "postulant",
//            ),
//    )
//}
