package andriesfc.powertoys.foundation


private typealias BadStateMessage = () -> String


inline fun checkState(conditionToBeTrue: Boolean, badStateMessage: BadStateMessage) {
    if (!conditionToBeTrue)
        throw IllegalStateException(badStateMessage())
}


inline fun <T> T.requiresValueOf(value: T, badStateMessage: BadStateMessage): T {
    checkState(this == value, badStateMessage)
    return this
}