package com.khanalytic.database.shared

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory constructor() {
    fun createDriver(): SqlDriver
}