package com.kuaapps.smstoemail.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SmtpData")
data class SmtpData (

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var encryptedHost: ByteArray,
    var encryptedPort: ByteArray,
    var encryptedUsername: ByteArray,
    var encryptedPassword: ByteArray

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SmtpData) return false

        return id == other.id &&
                encryptedHost.contentEquals(other.encryptedHost) &&
                encryptedPort.contentEquals(other.encryptedPort) &&
                encryptedUsername.contentEquals(other.encryptedUsername) &&
                encryptedPassword.contentEquals(other.encryptedPassword)

    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + encryptedHost.contentHashCode()
        result = 31 * result + encryptedPort.contentHashCode()
        result = 31 * result + encryptedUsername.contentHashCode()
        result = 31 * result + encryptedPassword.contentHashCode()
        return result
    }
}