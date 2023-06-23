package master.kotlin.coroutines

import kotlinx.coroutines.*

class Basics {
    @Volatile
    var sharedCounter = 0

    @OptIn(DelicateCoroutinesApi::class)
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
            println("World 2")
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
                    for (i in 1..1000) {
                        sharedCounter++
                    }
                }
            }
            coroutines.forEach { corotuine ->
                corotuine.join()
            }
        }.join()

        println("The number of shared counter should be 1000000, but actually is $sharedCounter")
    }

    suspend fun perforrmTestThree() {
        val tasks = arrayOf(
                longArrayOf(3, 7, 0, 2),
                longArrayOf(2, 4, 0, 8),
                longArrayOf(5, 8, 1, 2),
        )
        resolveTasks(tasks)
    }

    private suspend fun resolveMicroTasks(index: Int, microTasks: LongArray) {
        println("Task#$index running and should complete in ${microTasks.sum()} secs")
        val startTime = System.currentTimeMillis()
        microTasks.forEach { microTask ->
            delay(microTask * 1000)
            100 / microTask
        }
        println("Task#$index completed in ${(System.currentTimeMillis() - startTime) / 1000}")
    }

    private suspend fun resolveTasks(tasks: Array<LongArray>) {
        val completedTaskSet = mutableSetOf<Pair<Int, String>>()
        val failedTaskSet = mutableSetOf<Pair<Int, String>>()
        val taskJobs = tasks.mapIndexed { index, multiTasks ->
            Pair(index, CoroutineScope(Dispatchers.IO).launch {
                try {
                    resolveMicroTasks(index, multiTasks)
                    completedTaskSet.add(Pair(index, "SUCCESS"))
                } catch (e: ArithmeticException) {
                    failedTaskSet.add(Pair(index, e.localizedMessage))
                }
            })
        }
        taskJobs.forEach { task ->
            task.second.join()
        }
        for(task in completedTaskSet) {
            println("[SUCCESS]::TASK${task.first} completed.")
        }
        for(task in failedTaskSet) {
            println("[ERROR]::TASK${task.first} failed. Reason ${task.second}")
        }
        println("Tasks have been processed")
    }

    private suspend fun getThreadIdByPhoneNumber(phoneNumber: String) {
        println("Fetching the threadId by '$phoneNumber'...")
        delay(2000)
        println("Fetched the threadId by '$phoneNumber'.")
    }

    private suspend fun syncMessages(messages: List<String>) {
        messages.forEach {
            println("[LOCAL_PROGRESS]: Syncing the message '${it}'...")
            delay(3000)
            println("[LOCAL_COMPLETED]: Synced the message '${it}'")
        }
    }

    private fun sendMessages(messages: List<String>) {
        messages.forEach {
            println("[REMOTE_PROGRESS]: Sending the message '${it}'...")
            println("[REMOTE_COMPLETED]: Sent the message '${it}'")
        }
    }

    suspend fun handleAttachmentsForSelectedContact(phoneNumber: String) {
        getThreadIdByPhoneNumber(phoneNumber)
        println("Mapping objects to be later inserted to the database...")
        val messages = listOf("Apple", "Orange", "Banana")
        syncMessages(messages)
        sendMessages(messages)
    }
}