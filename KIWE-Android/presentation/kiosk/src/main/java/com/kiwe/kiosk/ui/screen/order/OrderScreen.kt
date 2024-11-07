package com.kiwe.kiosk.ui.screen.order

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.kiosk.R
import com.kiwe.kiosk.model.OrderItem
import com.kiwe.kiosk.ui.screen.order.component.CategorySelector
import com.kiwe.kiosk.ui.screen.order.component.OrderItem
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber

@Composable
fun OrderScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    shoppingCartViewModel: ShoppingCartViewModel,
    onEnterScreen: (Int) -> Unit,
) {
    val categoryStatus = viewModel.collectAsState().value
    val itemStatus = viewModel.collectAsState().value
    var isOrderOptionDialogOpen by remember { mutableStateOf(false) }
    var orderDialogMenuTitle by remember { mutableStateOf("") }
    var orderDialogMenuCost by remember { mutableIntStateOf(0) }
    val orderList by remember {
        derivedStateOf {
            itemStatus.orderItem.chunked(6)
        }
    }
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is OrderSideEffect.Toast ->
                Toast
                    .makeText(
                        context,
                        sideEffect.message,
                        Toast.LENGTH_SHORT,
                    ).show()

            OrderSideEffect.NavigateToNextScreen -> {
            }
        }
    }

    val coroutine = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutine.launch {
            viewModel.getCategoryList("디카페sss인")
        }
    }
    val animationScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        onEnterScreen(2)
    }
    val pagerState =
        rememberPagerState(pageCount = {
            orderList.size
        })

    val buttonState by remember {
        derivedStateOf {
            if (pagerState.currentPage == 0 && pagerState.currentPage == pagerState.pageCount - 1) {
                3 // 첫 페이지이자 마지막 페이지인 경우 특수 케이스
            } else if (pagerState.currentPage == pagerState.pageCount - 1) {
                2 // 마지막 페에지라서 더이상 밀 곳이 없을 때
            } else if (pagerState.currentPage == 0) {
                0 // 시작 부분으로 더이상 뒤로 갈 곳이 없을 때
            } else {
                1 // 일반적인 상태
            }
        }
    }
    val optionViewModel =
        hiltViewModel(
            creationCallback = { factory: OptionViewModel.OptionViewModelFactory ->
                factory.create(orderDialogMenuTitle, orderDialogMenuCost)
            },
        )
    LaunchedEffect(isOrderOptionDialogOpen) {
        if (isOrderOptionDialogOpen) {
            optionViewModel.init(orderDialogMenuTitle, orderDialogMenuCost)
        } else {
            optionViewModel.onClear()
        }
    }
    ShowDialog(
        show = isOrderOptionDialogOpen,
        onClose = { isOrderOptionDialogOpen = false },
        optionViewModel = optionViewModel,
        shoppingCartViewModel = shoppingCartViewModel,
    )

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp),
    ) {
        CategorySelector(
            modifier = Modifier,
            categoryState = categoryStatus.selectedCategory,
            onCategoryClick = { selectedCategory ->
                viewModel.setCategory(selectedCategory)
                Timber.tag(javaClass.simpleName).d("selectedCategory: $selectedCategory")
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1F),
            ) { index ->
                OrderListScreen(
                    orderItemList = orderList[index].chunked(3),
                    onItemClick = { title, cost ->
                        orderDialogMenuTitle = title
                        orderDialogMenuCost = cost
                        isOrderOptionDialogOpen = true
                    },
                )
            }
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 70.dp, end = 70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                if (buttonState == 0 || buttonState == 3) {
                    Spacer(modifier = Modifier.weight(1F))
                } else {
                    IconButton(
                        onClick = {
                            animationScope.launch {
                                pagerState.animateScrollToPage(
                                    (pagerState.currentPage - 1).coerceAtMost(
                                        0,
                                    ),
                                )
                            }
                        },
                        modifier = Modifier.weight(1F),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_left),
                            contentDescription = "이전",
                        )
                    }
                }
                Text(
                    modifier =
                        Modifier
                            .weight(1F)
                            .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    text = "${pagerState.currentPage + 1}/${pagerState.pageCount}",
                )
                if (buttonState == 2 || buttonState == 3) {
                    Spacer(modifier = Modifier.weight(1F))
                } else {
                    IconButton(onClick = {
                        animationScope.launch {
                            pagerState.animateScrollToPage(
                                (pagerState.currentPage + 1).coerceAtMost(
                                    pagerState.pageCount - 1,
                                ),
                            )
                        }
                    }, modifier = Modifier.weight(1F)) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_right),
                            contentDescription = "다음",
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderListScreen(
    orderItemList: List<List<OrderItem>>,
    onItemClick: (String, Int) -> Unit,
) {
    val firstRowList = orderItemList[0]
    val secondRowList =
        if (orderItemList.size > 1) {
            orderItemList[1]
        } else {
            emptyList()
        }

    Column {
        Row(modifier = Modifier.weight(1F)) {
            firstRowList.forEach {
                OrderItem(orderItem = it, modifier = Modifier.weight(1F), onClick = onItemClick)
            }
            // 개수가 3개보다 적을 때 채울 빈칸
            (firstRowList.size until 3).forEach { _ ->
                Spacer(modifier = Modifier.weight(1F))
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Row(modifier = Modifier.weight(1F)) {
            secondRowList.forEach {
                OrderItem(orderItem = it, modifier = Modifier.weight(1F), onClick = onItemClick)
            }
            (secondRowList.size until 3).forEach { _ ->
                Spacer(modifier = Modifier.weight(1F))
            }
        }
    }
}

@Composable
private fun ShowDialog(
    show: Boolean,
    onClose: () -> Unit,
    shoppingCartViewModel: ShoppingCartViewModel,
    optionViewModel: OptionViewModel,
) {
    if (show) {
        OptionDialog(
            shoppingCartViewModel = shoppingCartViewModel,
            onClose = onClose,
            optionViewModel = optionViewModel,
        )
    }
}

@Preview
@Composable
private fun OrderScreenPreview() {
    OrderScreen(
        viewModel = hiltViewModel(),
        shoppingCartViewModel = hiltViewModel(),
        onEnterScreen = {},
    )
}
