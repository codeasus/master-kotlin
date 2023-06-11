package master.kotlin.coroutines

import kotlinx.coroutines.*

class Basics {
    @Volatile var sharedCounter = 0
    fun perform() = runBlocking {
        performTestTwo()
    }

    private suspend fun performHelloWorld() = coroutineScope {
        launch {
            delay(3000)
            println("World 1")
        }

        launch {
            delay(5000)
            println ("World 2")
        }
        println("Hello")
    }

    private suspend fun performTestOne() = coroutineScope {
        val job = launch {
            delay(4000)
            println("Job start")
        }
        println("Outer print function")
        job.join()
        println("Done")
    }

    @DelicateCoroutinesApi
    private fun performTestTwo() = runBlocking {
        val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
        scope.launch {
            val coroutines = 1.rangeTo(1000).map {
                launch {
                    for(i in 1..1000){
                        sharedCounter++
                    }
                }
            }
            coroutines.forEach {
                corotuine->corotuine.join()
            }
        }.join()

        println("The number of shared counter should be 1000000, but actually is $sharedCounter")
    }
}