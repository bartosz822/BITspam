import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.classification.NaiveBayes
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.regression.LabeledPoint


val conf = new SparkConf().setMaster("local").setAppName("naivebayes")
val sc = new SparkContext(conf)

val htf = new HashingTF(10000)
val data = sc.textFile("/home/bartek/Dropbox/Studia/Scala/sparkTrainer/project/trainMails/trainData.csv")
val parsedData = data.map { line =>
  val label = line.takeWhile(_ != ',')
  val content = line.dropWhile(_ != ',').drop(1)
  LabeledPoint(label.toDouble, htf.transform(content.split(" ")))
}

val Array(training, test) = parsedData.randomSplit(Array(0.75, 0.25))
val model = NaiveBayes.train(training, lambda = 1.0, modelType = "multinomial")
val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))
val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()

val testData = htf.transform("Hello, Your certificate (or certificates) for the names listed below will expire in 19 days (on 15 May 17 18:57 +0000). Please make sure to renew your certificate before then, or visitors to your website will encounter errors. accounts.iiet.pl chat.iiet.pl forum.iiet.pl git.iiet.pl iiet.pl mail.iiet.pl postfix.iiet.pl wiki.iiet.pl ".split(" "))
model.predict(testData)
print(accuracy)
model.save(sc, "/home/bartek/Dropbox/Studia/Scala/sparkTrainer/model")