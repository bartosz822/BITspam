package model.service

import model.IsRelevantClassifier
import org.scalatestplus.play._

/**
  * Created by bartek on 28.06.17.
  */
class IsRelevantClassifierSpec extends PlaySpec {

  "IsRelevantClassifier " should {
    "Classiffy messeage as irrelevant" in {
      val classifier = new IsRelevantClassifier()
      val message = "Elo co tam w kole słychać pozdro"
      val resuslt = classifier.classify(message)

      resuslt mustBe "0.0"
    }


    "Classiffy messeage as relevant" in {
      val classifier = new IsRelevantClassifier()
      val message = "Witam, zglaszam się do państwa z ofertą wyjątkowych warsztatów" +
        "z javascripta, proszę podać listę zainteresowanych na maila"
      val resuslt = classifier.classify(message)

      resuslt mustBe "1.0"
    }

  }

}
