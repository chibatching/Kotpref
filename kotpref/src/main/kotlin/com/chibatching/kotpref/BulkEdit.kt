package com.chibatching.kotpref

public inline fun <T : KotprefModel> T.bulk(block: T.() -> Unit) {
    beginBulkEdit()
    try {
        block()
    } catch (e: Exception) {
        cancelBulkEdit()
        throw e
    }
    commitBulkEdit()
}

public inline fun <T : KotprefModel> T.blockingBulk(block: T.() -> Unit) {
    beginBulkEdit()
    try {
        block()
    } catch (e: Exception) {
        cancelBulkEdit()
        throw e
    }
    blockingCommitBulkEdit()
}
