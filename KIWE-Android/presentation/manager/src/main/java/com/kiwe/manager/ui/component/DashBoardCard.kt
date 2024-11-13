package com.kiwe.manager.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kiwe.manager.R

@Composable
fun DashBoardCard(
    modifier: Modifier,
    iconId: Int,
    title: String,
    value: String,
    differenceIcon: Int,
    differenceColor: Int,
    difference: String,
) {
    Card(
        modifier = modifier,
        colors =
            CardDefaults.cardColors(
                containerColor = colorResource(R.color.dashboard_card_background),
            ),
        shape = RoundedCornerShape(20.dp),
    ) {
        Box(
            modifier = Modifier.padding(18.dp),
        ) {
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                    painter = painterResource(iconId),
                    contentDescription = "",
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    title,
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    value,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(differenceIcon),
                        contentDescription = "",
                        tint = colorResource(differenceColor),
                    )
                    Text(
                        difference,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
