package com.yavin.cashlessDemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.yavin.cashlessDemo.model.CashlessPaymentInput
import com.yavin.cashlessDemo.model.TagInfo
import com.yavin.cashlessDemo.model.remote.YavinNFCReaderResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString


class CashlessPaymentResultContract : ActivityResultContract<CashlessPaymentInput, TagInfo?>() {
    override fun createIntent(context: Context, input: CashlessPaymentInput): Intent =
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("yavin://com.yavin.macewindu/v4/nfc-reader")
        }

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.extras?.getString("response")?.let {
            Json.decodeFromString<YavinNFCReaderResponse>(it).tagInfo
        }

}