package andriesfc.powertoys.foundation

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

interface SystemTime {
    fun now(): Instant
    fun date(): LocalDate = LocalDate.ofInstant(now(), ZoneId.systemDefault())
    fun dateTime(): LocalDateTime = LocalDateTime.ofInstant(now(), ZoneId.systemDefault())
    fun time(): LocalTime = LocalTime.ofInstant(now(), ZoneId.systemDefault())
}

object HostSystemTime : SystemTime {
    override fun now(): Instant = Instant.now()
}

