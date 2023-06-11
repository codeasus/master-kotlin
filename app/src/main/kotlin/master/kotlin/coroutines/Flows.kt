package master.kotlin.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class NumberRepository {
    fun getNumber() = flow {
        var currentValue = 10
        while (currentValue >= 0) {
            emit(currentValue--)
            delay(1000L)
        }
    }
}

class NumberFormatManagementService {
    private val _numberRepository = NumberRepository()
    private val _formattedNumber = MutableSharedFlow<String>()
    val formattedNumber = _formattedNumber.asSharedFlow()
    suspend fun fetchNumber() {
        _numberRepository
                .getNumber()
                .map { "The number: $it" }
                .onEach {
                    _formattedNumber.emit(it)
                }
                .collect()
    }

    fun getFormattedNumber(): String {
        return ""
    }
}