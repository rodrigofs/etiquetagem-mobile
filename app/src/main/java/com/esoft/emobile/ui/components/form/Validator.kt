package com.esoft.emobile.ui.components.form

abstract class Validator<T>(
    val validate: (s: T?) -> Boolean,
    val errorText: String
)
