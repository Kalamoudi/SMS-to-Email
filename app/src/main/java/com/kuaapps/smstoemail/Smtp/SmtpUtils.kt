package com.kuaapps.smstoemail.Smtp

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kuaapps.smstoemail.Entity.SmtpData
import com.kuaapps.smstoemail.R
import com.kuaapps.smstoemail.Utils
import com.kuaapps.smstoemail.sharedPrefs

object SmtpUtils {

    private var encryptionKey = Utils.getEncryptionKey()

    fun addAdvertisement(context: Context){
        if(sharedPrefs.getBoolean("advertisementOff", true)){
            return
        }

        val smallAdvertisement: View = (context as AppCompatActivity).findViewById(R.id.adViewInSmtp)

        smallAdvertisement.visibility = View.VISIBLE


    }

    fun encryptSmtpData(host: String, port: String, username: String, password: String): SmtpData {
        val smtpInSmtpData = SmtpData(
            0,
            Utils.encryptData(host.toByteArray(), encryptionKey),
            Utils.encryptData(port.toByteArray(), encryptionKey),
            Utils.encryptData(username.toByteArray(), encryptionKey),
            Utils.encryptData(password.toByteArray(), encryptionKey)
        )

        return smtpInSmtpData

    }

    fun decryptSmtpData(smtpDataList: List<SmtpData>): DecryptedSmtpData {

        val encryptedSmtpData = smtpDataList[0]
        val decryptedSmtpData = DecryptedSmtpData(
            encryptedSmtpData.id,
            Utils.decryptData(encryptedSmtpData.encryptedHost, encryptionKey).toString(Charsets.UTF_8),
            Utils.decryptData(encryptedSmtpData.encryptedPort, encryptionKey).toString(Charsets.UTF_8),
            Utils.decryptData(encryptedSmtpData.encryptedUsername, encryptionKey).toString(Charsets.UTF_8),
            Utils.decryptData(encryptedSmtpData.encryptedPassword, encryptionKey).toString(Charsets.UTF_8)
        )
        return decryptedSmtpData

    }

}