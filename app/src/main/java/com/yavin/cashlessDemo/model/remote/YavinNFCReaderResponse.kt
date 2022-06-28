package com.yavin.cashlessDemo.model.remote

import com.yavin.cashlessDemo.model.TagInfo
import kotlinx.serialization.Serializable

@Serializable
data class YavinNFCReaderResponse(val status: Boolean, val tagInfo: TagInfo?)
