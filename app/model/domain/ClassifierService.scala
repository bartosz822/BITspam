package model.domain

/**
  * Created by bartek on 11.06.17.
  */
trait ClassifierService {

  // classifier should return double value representing classification
  def Classify(text: String): Double

}
