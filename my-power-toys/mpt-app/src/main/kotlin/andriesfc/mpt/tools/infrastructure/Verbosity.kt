package andriesfc.mpt.tools.infrastructure

enum class Verbosity(val switch:String) {

    Silent("--silent"),
    Error("--error"),
    Warn("--warn"),
    Debug("--debug");

    companion object
}