package com.nova.assistant

sealed class NovaIntent {
    data class Call(val contact: String) : NovaIntent()
        data class OpenApp(val appName: String) : NovaIntent()
            data class SendMessage(val contact: String, val body: String) : NovaIntent()
                object FlashlightOn : NovaIntent()
                    object FlashlightOff : NovaIntent()
                        object BluetoothOn : NovaIntent()
                            object BluetoothOff : NovaIntent()
                                object MobileData : NovaIntent()
                                    data class Query(val text: String) : NovaIntent()
                                        object Unknown : NovaIntent()
                                            object Greeting : NovaIntent()
                                            }