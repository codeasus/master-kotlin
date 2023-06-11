package master.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.collectLatest
import master.kotlin.coroutines.Basics
import master.kotlin.coroutines.NumberFormatManagementService
import java.lang.Runnable

class App {
    fun run() {
        Basics().perform()
    }
}

class BasicTaskScheduler : Runnable {
    private var isRunning = true

    override fun run() {
        while (isRunning) {
            print("*")
            Thread.sleep(500)
        }
        println("\nTask completed.")
    }

    fun stop() {
        isRunning = false
    }
}

suspend fun suspendingNameList(): List<String> = buildList {
    for (i in 0 until 10) {
        this.add("$i")
        delay(1000)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.nameProducingChannel() = produce {
    for (i in 0 until 10) {
        this.send("produced_str_${i}")
        delay(1000)
    }
}

fun main() {
    val numberFormatManagementService = NumberFormatManagementService()
    runBlocking {
        launch {
            numberFormatManagementService.fetchNumber()
        }
        numberFormatManagementService.formattedNumber.collectLatest {
            println(":: $it")
        }
//        delay(3000)
//        print("Invoked: ${numberFormatManagementService.getFormattedNumber()}")
    }
}