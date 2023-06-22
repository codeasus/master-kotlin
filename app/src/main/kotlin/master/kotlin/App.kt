package master.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import master.kotlin.coroutines.Basics
import java.lang.Runnable

class App {
    fun run() {
        Basics()
    }

    suspend fun runSuspend() {
        Basics().perforrmTestThree()
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
    runBlocking {
        App().runSuspend()
    }
}