package com.khanalytic.database.shared

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val dbQuery = AppDatabase(databaseDriverFactory.createDriver()).appDatabaseQueries
}