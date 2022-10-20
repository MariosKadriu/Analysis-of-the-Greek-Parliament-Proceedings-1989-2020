package com.BigData.spark

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel, IDF}
import org.apache.spark.ml.linalg.DenseVector
import org.apache.spark.ml.clustering.LDA
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window

class task_3 {
  def run(spark:SparkSession, tokenized:DataFrame) {
    import spark.implicits._

    val members = tokenized
      .filter(col("sitting_date") >= lit(2015) && col("sitting_date") <= lit(2020))
      .groupBy("member_name", "sitting_date").agg(flatten(collect_list("words")).alias("speeches"))
      .orderBy("member_name", "sitting_date")

    val vectorizer_members: CountVectorizerModel = new CountVectorizer()
      .setInputCol("speeches")
      .setOutputCol("raw_features")
      .setVocabSize(10000)
      .setMinDF(5)
      .fit(members)
	  
    val countVectors_members = vectorizer_members.transform(members)

    val idf_members = new IDF()
      .setInputCol("raw_features")
      .setOutputCol("features")
      .fit(countVectors_members)
      .transform(countVectors_members)

    print("Type the number of topics: ")
    val numTopics_members = scala.io.StdIn.readInt()

    val lda_members = new LDA()
      .setK(numTopics_members)
      .setMaxIter(3)
      .fit(idf_members)

    // Most important keywords by member
    var results_members = lda_members.transform(idf_members)

    val toArr: Any => Array[Double] = _.asInstanceOf[DenseVector].toArray
    val toArrUdf = udf(toArr)
    results_members = results_members.withColumn("topicDistribution", toArrUdf('topicDistribution))

    val w = Window.partitionBy($"member_name", $"sitting_date").orderBy($"member_name", $"sitting_date".desc, $"word_distribution".desc)

    results_members
      .withColumn("vars", explode(arrays_zip($"speeches", $"topicDistribution")))
      .select($"member_name", $"sitting_date", $"vars.speeches", $"vars.topicDistribution")
      .filter($"topicDistribution".isNotNull && $"speeches".isNotNull)
      .withColumnRenamed("speeches", "word")
      .withColumnRenamed("topicDistribution", "word_distribution")
      .withColumn("rn", row_number.over(w)).where($"rn" <= 3).drop("rn")
      .show(1000,false)
  }
}