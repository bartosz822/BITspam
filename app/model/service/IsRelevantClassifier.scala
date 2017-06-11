package model.service

import model.domain.ClassifierService
import org.apache.spark.mllib.classification.NaiveBayesModel
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.{SparkConf, SparkContext}

class IsRelevantClassifier extends ClassifierService{

  val conf: SparkConf = new SparkConf().setMaster("local").setAppName("naivebayes")
  val sc: SparkContext = new SparkContext(conf)

  val htf: HashingTF = new HashingTF(10000)
  val model: NaiveBayesModel = NaiveBayesModel.load(sc, "conf/model")

  def Classify(text: String): Double ={
    model.predict(htf.transform(text))
  }

}

