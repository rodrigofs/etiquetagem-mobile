package com.esoft.emobile.ui.components.form.formatters

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun dateShort(r: LocalDate?): String {
    return if (r != null) DateFormat.getDateInstance().format(r) else ""
}

fun formatDateToBR(date: Date?): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
    return date?.let { dateFormat.format(it) } ?: ""
}

fun dateBr(r: LocalDate?): String {
    val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return r?.format(dateFormat)?.toString() ?: ""
}

fun timeBr(r: LocalTime?): String {
    val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return r?.format(dateFormat)?.toString() ?: ""
}

fun stringToTime(time: String?): LocalTime? {
    val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return time?.let {
        LocalTime.parse(it, timeFormat)
    }
}

fun stringToDate(date: String?): LocalDate? {
    val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return date?.let {
        LocalDate.parse(it, dateFormat)
    }
}

fun stringToDateTime(dateTime: String?): LocalDateTime? {
    val dateTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return dateTime?.let {
        LocalDateTime.parse(it, dateTimeFormat)
    }
}