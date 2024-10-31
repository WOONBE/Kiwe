package com.kiwe.manager.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.kiwe.manager.R

@Composable
fun LoginAppBar() {
    val constraintSet =
        ConstraintSet {
            val logoImageRef = createRefFor("logoImage")
            val appTitle = createRefFor("appTitle")

            constrain(logoImageRef) {
                start.linkTo(parent.start)
                end.linkTo(appTitle.start)
                top.linkTo(appTitle.top)
                bottom.linkTo(appTitle.bottom)
                height = Dimension.fillToConstraints
                horizontalBias = 0F
            }
            constrain(appTitle) {
                start.linkTo(logoImageRef.end)
                end.linkTo(parent.end)
            }
            createHorizontalChain(logoImageRef, appTitle, chainStyle = ChainStyle.Packed)
        }
    Column {
        Row(
            modifier = Modifier.height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.weight(1F))
            ConstraintLayout(
                constraintSet = constraintSet,
                modifier = Modifier.weight(32F),
            ) {
                Image(
                    modifier = Modifier.layoutId("logoImage"),
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "로고",
                )
                Text(
                    modifier = Modifier.layoutId("appTitle"),
                    text = "KIWE",
                )
            }
        }
        HorizontalDivider(thickness = 2.dp)
    }
}
