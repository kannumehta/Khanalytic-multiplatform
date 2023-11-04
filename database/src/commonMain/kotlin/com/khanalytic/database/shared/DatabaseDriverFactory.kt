package com.khanalytic.database.shared

import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.component.KoinComponent

expect class DatabaseDriverFactory constructor() : KoinComponent {
    fun createDriver(): SqlDriver
}