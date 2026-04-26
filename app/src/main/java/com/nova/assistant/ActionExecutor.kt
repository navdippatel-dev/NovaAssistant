package com.nova.assistant

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.Settings
import android.telephony.SmsManager

class ActionExecutor(private val context: Context, private val speechService: SpeechService) {

    // FLASHLIGHT
    fun setFlashlight(on: Boolean) {
        try {
            val cm = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId = cm.cameraIdList.firstOrNull() ?: return
            cm.setTorchMode(cameraId, on)
            val msg = if (on) "Flashlight turned on" else "Flashlight turned off"
            speechService.speak(msg)
        } catch (e: Exception) {
            speechService.speak("Sorry, I couldn't control the flashlight")
        }
    }

    // BLUETOOTH
    @SuppressLint("MissingPermission")
    fun setBluetooth(on: Boolean) {
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
                val adapter = BluetoothAdapter.getDefaultAdapter()
                if (on) adapter?.enable() else adapter?.disable()
                val msg = if (on) "Bluetooth turned on" else "Bluetooth turned off"
                speechService.speak(msg)
            } else {
                speechService.speak("Opening Bluetooth settings")
                val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            speechService.speak("Sorry, I couldn't control Bluetooth")
        }
    }

    // MOBILE DATA
    fun openMobileDataSettings() {
        speechService.speak("Opening mobile data settings")
        val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    // MAKE CALL
    fun makeCall(contactName: String) {
        val number = resolveContact(contactName)
        if (number != null) {
            speechService.speak("Calling $contactName")
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            speechService.speak("I couldn't find $contactName in your contacts")
        }
    }

    // OPEN APP
    fun openApp(appName: String) {
        val packageMap = mapOf(
            "whatsapp" to "com.whatsapp",
            "youtube" to "com.google.android.youtube",
            "instagram" to "com.instagram.android",
            "chrome" to "com.android.chrome",
            "maps" to "com.google.android.apps.maps",
            "gmail" to "com.google.android.gm",
            "camera" to "com.android.camera2",
            "settings" to "com.android.settings",
            "spotify" to "com.spotify.music",
            "facebook" to "com.facebook.katana",
            "twitter" to "com.twitter.android",
            "netflix" to "com.netflix.mediaclient"
        )
        val pkg = packageMap[appName.lowercase()]
        if (pkg != null) {
            val intent = context.packageManager.getLaunchIntentForPackage(pkg)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                speechService.speak("Opening $appName")
                context.startActivity(intent)
            } else {
                speechService.speak("$appName is not installed on your phone")
            }
        } else {
            speechService.speak("I don't know how to open $appName")
        }
    }

    // SEND SMS
    fun sendSms(contactName: String, message: String) {
        val number = resolveContact(contactName)
        if (number != null) {
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(number, null, message, null, null)
                speechService.speak("Message sent to $contactName")
            } catch (e: Exception) {
                speechService.speak("Sorry, I couldn't send the message")
            }
        } else {
            speechService.speak("I couldn't find $contactName in your contacts")
        }
    }

    // HELPER — find phone number from contact name
    private fun resolveContact(name: String): String? {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val displayName = it.getString(1) ?: continue
                if (displayName.lowercase().contains(name.lowercase())) {
                    return it.getString(0)
                }
            }
        }
        return null
    }
}