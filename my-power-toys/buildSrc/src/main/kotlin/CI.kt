
val isCI:Boolean get() = System.getenv("CI")?.isNotBlank() ?: false
