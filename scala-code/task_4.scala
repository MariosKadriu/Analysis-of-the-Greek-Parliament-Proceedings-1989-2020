package com.BigData.spark

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.ml.feature.CountVectorizerModel
import org.apache.spark.ml.clustering.LDA
import org.apache.spark.sql.functions._
import scala.collection.mutable

class task_4 {
  def run(vectorizer:CountVectorizerModel, results:DataFrame){
    val term_to_voc_word = udf((arr:mutable.WrappedArray[Int]) => {
      arr.toArray.map(term => vectorizer.vocabulary(term))
    })

    print("Type the number of topics: ")
    val numTopics = scala.io.StdIn.readInt()

    println("Years 2004-2008 Before Crisis")
    val years_2004_2008 = results.filter(col("sitting_date") >= lit(2004) && col("sitting_date") <= lit(2008))

    val years_2004_2008_lda = new LDA()
      .setK(numTopics)
      .setMaxIter(5)
      .fit(years_2004_2008)

    // Describe topics for years 2004-2008 before crisis
    val years_2004_2008_topics = years_2004_2008_lda.describeTopics(20)

    val years_2004_2008_topics_terms = years_2004_2008_topics.withColumn("terms", term_to_voc_word(col("termIndices"))).select("topic", "terms")
    years_2004_2008_topics_terms.show(50,false)

    println("Years 2009-2013 After Crisis")
    val years_2009_2013 = results.filter(col("sitting_date") >= lit(2009) && col("sitting_date") <= lit(2013))

    val years_2009_2013_lda = new LDA()
      .setK(numTopics)
      .setMaxIter(5)
      .fit(years_2009_2013)

    // Describe topics for years 2009-2013 after crisis
    val years_2009_2013_topics = years_2009_2013_lda.describeTopics(20)

    val years_2009_2013_topics_terms = years_2009_2013_topics.withColumn("terms", term_to_voc_word(col("termIndices"))).select("topic", "terms")
    years_2009_2013_topics_terms.show(numTopics,false)
  }
}
