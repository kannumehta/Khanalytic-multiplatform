package com.khanalytic.database.shared

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Database: KoinComponent {
    private val databaseDriverFactory: DatabaseDriverFactory by inject()
    val dbQuery = AppDatabase(databaseDriverFactory.createDriver()).appDatabaseQueries
}