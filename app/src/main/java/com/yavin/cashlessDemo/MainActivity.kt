package com.yavin.cashlessDemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yavin.cashlessDemo.model.CashlessPaymentInput
import com.yavin.cashlessDemo.ui.theme.DemoCashlessTheme
import java.text.NumberFormat
import java.util.*

val formatter: NumberFormat = NumberFormat.getCurrencyInstance().apply {
    maximumFractionDigits = 2
    currency = Currency.getInstance("EUR")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoCashlessTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BillScreen()
                }
            }
        }
    }
}

@Composable
fun BillScreen() {
    val showDialog = remember {
        Log.e("Ludo", "Initial set")
        mutableStateOf(false)
    }
    val serialNumber = remember { mutableStateOf("") }

    val itemList = remember { dummyItems }

    val totalPrice by derivedStateOf {
        formatter.format(dummyItems.sumOf { it.totalPriceInCents } / 100.0)
    }

    val readLauncher =
        rememberLauncherForActivityResult(contract = CashlessPaymentResultContract()) {
            Log.e("Ludo", "TagInfo: $it")
            serialNumber.value = it?.serialNumber ?: ""
            showDialog.value = true
        }

    Column(Modifier.fillMaxSize()) {
        SmallTopAppBar(title = { Text(text = "L'addition") })
        Divider(Modifier.height(1.dp))
        Column(Modifier.padding(16.dp)) {
            LazyColumn {
                itemsIndexed(itemList, key = { index, item ->
                    item.name
                }) { index, item ->
                    ItemRow(data = item)
                    if (index < itemList.lastIndex) {
                        Divider()
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Divider()
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Total $totalPrice",
                textAlign = TextAlign.End
            )
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                readLauncher.launch(CashlessPaymentInput())
            }) {
                Text(text = "Payer") //TODO le montant dans le bouton
            }
        }
    }

    Log.e("Ludo", "Recompose ${showDialog.value}")

    if (showDialog.value) {
        AlertDialog(onDismissRequest = {
            showDialog.value = false
        }, title = {
            Text(text = "Paiement")
        }, confirmButton = {
            Button(onClick = { showDialog.value = false }) {
                Text(text = "OK")
            }
        }, text = {
            if (serialNumber.value.isNotEmpty()) {
                Column {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Paiement effectué avec la carte",
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = serialNumber.value,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Text(text = "La carte n'a pas pu être lue")
            }
        })
    }
}

@Composable
fun ItemRow(data: Item) {
    data.run {
        val priceInEuros by derivedStateOf {
            val priceInEuros = totalPriceInCents / 100.0
            formatter.format(priceInEuros)
        }

        Row(Modifier.fillMaxWidth()) {
            Text(text = "${quantity}x")
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = name)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = priceInEuros)

        }
    }
}

data class Item(val name: String, val totalPriceInCents: Int, val quantity: Int)

val dummyItems = listOf(
    Item("Bière", 1500, quantity = 3),
    Item("Coca cola", 500, quantity = 1),
    Item("Planche à partager", 2000, quantity = 1),
)

@Preview
@Composable
fun PreviewBillScreen() {
    DemoCashlessTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BillScreen()
        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DemoCashlessTheme {
        Greeting("Android")
    }
}