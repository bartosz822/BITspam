package model.service

import model.domain.classification.ClassifierService
import org.apache.spark.mllib.classification.NaiveBayesModel
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.{SparkConf, SparkContext}

class IsRelevantClassifier extends ClassifierService{

  val conf: SparkConf = new SparkConf().setMaster("local").setAppName("naivebayes")
  val sc: SparkContext = new SparkContext(conf)

  val htf: HashingTF = new HashingTF(10000)
  val model: NaiveBayesModel = NaiveBayesModel.load(sc, "conf/model")

  def classify(text: String): Double ={
    val res = model.predict(htf.transform(text.replaceAll("""<(?!\/?a(?=>|\s.*>))\/?.*?>""", "").split(" ")))
    println(text.replaceAll("""<(?!\/?a(?=>|\s.*>))\/?.*?>""", ""))
    println(res)
    res
  }

}

