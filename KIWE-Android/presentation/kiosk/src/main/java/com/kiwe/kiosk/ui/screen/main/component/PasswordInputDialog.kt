package com.kiwe.kiosk.ui.screen.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KiweBlack1
import com.kiwe.kiosk.ui.theme.KiweGray1
import com.kiwe.kiosk.ui.theme.KiweOrange1
import com.kiwe.kiosk.ui.theme.KiweWhite1
import com.kiwe.kiosk.ui.theme.Typography

@Composable
fun CustomPasswordInputDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var password by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties =
            DialogProperties(
                dismissOnBackPress = false, // 뒤로가기 눌러도 닫히지 않음
                dismissOnClickOutside = false, // 외부 클릭으로 닫히지 않음
            ),
    ) {
        Box(
            modifier =
                modifier
                    .fillMaxWidth()
                    .background(KiweWhite1) // 원하는 배경색 설정
                    .padding(16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "로그아웃 확인",
                    color = KiweBlack1,
                    style = Typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "로그아웃하려면 비밀번호를 입력하세요.",
                    color = KiweGray1,
                    style = Typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier.padding(10.dp),
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("비밀번호", color = KiweOrange1, style = Typography.bodyMedium) },
                    placeholder = { Text("비밀번호를 입력해주세요") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = KiweWhite1,
                            unfocusedContainerColor = KiweWhite1,
                            disabledContainerColor = KiweWhite1,
                            focusedIndicatorColor = KiweOrange1,
                            focusedLabelColor = KiweOrange1,
                        ),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Button(
                        onClick = { onDismissRequest() },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color.Gray,
                                contentColor = Color.White,
                            ),
                        shape = RectangleShape, // 버튼을 사각형 모양으로 설정
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(50.dp), // 버튼 높이를 증가
                    ) {
                        Text("취소")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(password) },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = KiweOrange1,
                                contentColor = Color.White,
                            ),
                        shape = RectangleShape, // 버튼을 사각형 모양으로 설정
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(50.dp), // 버튼 높이를 증가
                    ) {
                        Text("확인")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomPasswordInputDialog() {
    KIWEAndroidTheme {
        CustomPasswordInputDialog(
            onDismissRequest = {},
            onConfirm = { /* Do something */ },
        )
    }
}
