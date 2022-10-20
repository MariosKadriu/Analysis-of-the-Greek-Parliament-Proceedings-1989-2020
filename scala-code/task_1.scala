package com.BigData.spark

import org.apache.spark.ml.clustering.LDA
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.ml.feature.CountVectorizerModel
import org.apache.spark.sql.functions.{col, lit, max, min, udf}
import scala.collection.mutable
import scala.util.control.Breaks

class task_1 {
  def run(tokenized:DataFrame, vectorizer:CountVectorizerModel, results:DataFrame) {
    val Row(minValue: Int, maxValue: Int) = tokenized.agg(min("sitting_date"), max("sitting_date")).head

    // User-defined function that converts the weights of each term into the word corresponding to the vocabulary
    val term_to_voc_word = udf((arr:mutable.WrappedArray[Int]) => {
      arr.toArray.map(term => vectorizer.vocabulary(term))
    })

    print("Type the number of topics: ")
    val numTopics = scala.io.StdIn.readInt()

    val loop = new Breaks;

    for(year <- minValue to maxValue){
      loop.breakable{
        if(year == 1995) loop.break() // No data for year 1995 so we skip it

        println(s"Year: $year")

        val results_year = results.filter(col("sitting_date") === lit(year))

        // Create the LDA model and fit the results constant that was established before
        val lda = new LDA()
          .setK(numTopics)
          .setMaxIter(5)
          .fit(results_year)

        // Describe topics.
        val topics = lda.describeTopics(10)

        val topics_terms = topics.withColumn("terms", term_to_voc_word(col("termIndices"))).select("topic", "terms")
        topics_terms.show(numTopics, false)
      }
    }
  }
}
