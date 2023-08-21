package andriesfc.powertoys.foundation


inline fun <T> Boolean.map(truth: () -> T, notTrue: () -> T): T {
    return when {
        this -> truth()
        else -> notTrue()
    }
}


fun Boolean.mapToInt(truth: Int = 1, notTrue: Int = 0): Int = map({ truth }, { notTrue })

