package com.nova.assistant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AssistantViewModel(application: Application) : AndroidViewModel(application) {

    enum class ListeningState { IDLE, LISTENING, PROCESSING }

    val listeningState = MutableLiveData(ListeningState.IDLE)
    val responseText = MutableLiveData("")
    val transcriptText = MutableLiveData("")

    private val speechService = SpeechService(application)
    private val commandProcessor = CommandProcessor()
    private val actionExecutor = ActionExecutor(application, speechService)

    init {
        speechService.onListeningStarted = {
            listeningState.postValue(ListeningState.LISTENING)
        }
        speechService.onListeningStopped = {
            listeningState.postValue(ListeningState.PROCESSING)
        }
        speechService.onResult = { transcript ->
            transcriptText.postValue(transcript)
            handleTranscript(transcript)
        }
    }

    fun startListening() {
        listeningState.value = ListeningState.LISTENING
        speechService.speak("")
        speechService.startListening()
    }

    private fun handleTranscript(transcript: String) {
        listeningState.postValue(ListeningState.PROCESSING)
        viewModelScope.launch {
            val intent = commandProcessor.parse(transcript)
            executeIntent(intent)
            listeningState.postValue(ListeningState.IDLE)
        }
    }

    private fun executeIntent(intent: NovaIntent) {
        when (intent) {
            is NovaIntent.Call -> actionExecutor.makeCall(intent.contact)
            is NovaIntent.OpenApp -> actionExecutor.openApp(intent.appName)
            is NovaIntent.SendMessage -> actionExecutor.sendSms(intent.contact, intent.body)
            is NovaIntent.FlashlightOn -> actionExecutor.setFlashlight(true)
            is NovaIntent.FlashlightOff -> actionExecutor.setFlashlight(false)
            is NovaIntent.BluetoothOn -> actionExecutor.setBluetooth(true)
            is NovaIntent.BluetoothOff -> actionExecutor.setBluetooth(false)
            is NovaIntent.MobileData -> actionExecutor.openMobileDataSettings()
            is NovaIntent.Query -> {
                speechService.speak("Sorry, I didn't understand that. Please try again.")
                responseText.postValue("Sorry, I didn't understand: ${intent.text}")
            }
            is NovaIntent.Unknown -> {
                speechService.speak("Sorry, I didn't understand that.")
                responseText.postValue("Unknown command")
            }
        }
    }

    fun speakWelcome() {
        speechService.speak("Hi! I am Nova. Tap the mic and give me a command.")
    }

    override fun onCleared() {
        super.onCleared()
        speechService.destroy()
    }
}