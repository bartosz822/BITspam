package model.service

import java.time.{LocalDateTime, ZoneId}
import java.util.Date

object TimeProvider {

  def current = LocalDateTime.now().atZone(ZoneId.systemDefault())

  def now = Date.from(current.toInstant)

}
