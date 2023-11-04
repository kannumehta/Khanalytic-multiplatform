package com.khanalytic.database.shared

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    val dbQuery = AppDatabase(databaseDriverFactory.createDriver()).appDatabaseQueries
}