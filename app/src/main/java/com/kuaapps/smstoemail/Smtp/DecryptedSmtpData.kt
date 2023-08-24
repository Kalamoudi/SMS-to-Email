package com.kuaapps.smstoemail.Smtp

data class DecryptedSmtpData(

    var id: Long,
    var host: String,
    var port: String,
    var username: String,
    var password: String
)

