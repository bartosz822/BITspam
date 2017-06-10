package model

import java.util.Date

package object domain {

  final case class EmailId(value: String)

  final case class EmailMetadata(id: EmailId, sender: String, sentTime: Date, receivedTime: Date)

}
