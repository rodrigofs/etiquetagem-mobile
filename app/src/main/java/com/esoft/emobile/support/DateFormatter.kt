package com.esoft.emobile.support

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun formatDate(dateUTC: String?) : String {
    if (dateUTC.isNullOrEmpty()) return ""

    val date = SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(dateUTC)
    val newFormat = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())
    return date?.let { newFormat.format(it) }.orEmpty()
}

fun dateTimeNowBR() : String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    return LocalDateTime.now().format(formatter)
}

fun dateTimeNowUS() : String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.now().format(formatter)
}


fun formatDatetimeToUS(date: String) : String? {
    val inputDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    val date2 = inputDateFormat.parse(date)

    return date2?.let { outputDateFormat.format(it) }
}