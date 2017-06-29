package model.service

import org.scalatestplus.play._

/**
  * Created by bartek on 28.06.17.
  */
class IsRelevantClassifierSpec extends PlaySpec {

  private val classifier = new IsRelevantClassifier()

  "IsRelevantClassifier " should {
    "Classiffy short messeage as irrelevant" in {
      val message = "Elo co tam w kole słychać pozdro"
      val resuslt = classifier.classify(message)

      resuslt mustBe "0.0"
    }

    "Classiffy random characters as irrelevant" in {
      val message = "akjhfkjdshfkljashlfkjhaslkjdfhaksldjhflkasjdhgkljasdgklafdhd"
      val resuslt = classifier.classify(message)

      resuslt mustBe "0.0"
    }


    "Classiffy empty message as irrelevant" in {
      val message = ""
      val resuslt = classifier.classify(message)

      resuslt mustBe "0.0"
    }


    "Classiffy messeage as relevant" in {
      val message = "Witam, zglaszam się do państwa z ofertą wyjątkowych warsztatów" +
        "z javascripta, proszę podać listę zainteresowanych na maila"
      val resuslt = classifier.classify(message)

      resuslt mustBe "1.0"
    }

  }

}
