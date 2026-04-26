package com.nova.assistant

class CommandProcessor {

    fun parse(transcript: String): NovaIntent {
        val t = transcript.lowercase().trim()

        return when {
            // FLASHLIGHT
            t.contains("turn on flashlight") || t.contains("flashlight on") ||
                    t.contains("torch on") || t.contains("turn on torch") ->
                NovaIntent.FlashlightOn

            t.contains("turn off flashlight") || t.contains("flashlight off") ||
                    t.contains("torch off") || t.contains("turn off torch") ->
                NovaIntent.FlashlightOff

            // BLUETOOTH
            t.contains("turn on bluetooth") || t.contains("bluetooth on") ||
                    t.contains("enable bluetooth") ->
                NovaIntent.BluetoothOn

            t.contains("turn off bluetooth") || t.contains("bluetooth off") ||
                    t.contains("disable bluetooth") ->
                NovaIntent.BluetoothOff

            // MOBILE DATA
            t.contains("mobile data") || t.contains("turn on data") ||
                    t.contains("turn off data") || t.contains("data settings") ->
                NovaIntent.MobileData

            // SEND MESSAGE
            t.startsWith("message ") || t.startsWith("send message") ||
                    t.startsWith("text ") -> {
                val cleaned = t.removePrefix("send message").removePrefix("message")
                    .removePrefix("text").trim()
                val parts = cleaned.split(" ", limit = 2)
                val contact = parts.getOrElse(0) { "" }
                val body = parts.getOrElse(1) { "hello" }
                NovaIntent.SendMessage(contact, body)
            }

            // OPEN APP
            t.startsWith("open ") || t.startsWith("launch ") ||
                    t.startsWith("start ") -> {
                val appName = t.removePrefix("open").removePrefix("launch")
                    .removePrefix("start").trim()
                NovaIntent.OpenApp(appName)
            }

            // CALL
            t.startsWith("call ") || t.startsWith("phone ") ||
                    t.startsWith("dial ") -> {
                val contact = t.removePrefix("call").removePrefix("phone")
                    .removePrefix("dial").trim()
                NovaIntent.Call(contact)
            }

            // FALLBACK
            else -> NovaIntent.Query(t)
        }
    }
}