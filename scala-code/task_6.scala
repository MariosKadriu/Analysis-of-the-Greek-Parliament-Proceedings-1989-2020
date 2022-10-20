package com.BigData.spark

import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel, IDF}
import org.apache.spark.ml.clustering.LDA
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Row
import scala.collection.mutable

class task_6 {
  def run(tokenized:DataFrame) {
    val Row(min_date: Int, max_date: Int) = tokenized.agg(min("sitting_date"), max("sitting_date")).head

    for(year <- min_date to max_date) {
      println(s"Year: $year")

      val gender_speeches_count = tokenized
        .filter(col("sitting_date") === lit(year))
        .groupBy("sitting_date", "member_gender").agg(collect_list("words").alias("speeches"))
        .withColumn("speeches_count", size(col("speeches")))
        .orderBy("member_gender","sitting_date")
      gender_speeches_count.show()
    }

    for(year <- min_date to max_date) {
      println(s"Year: $year")

      val gender_speeches_len = tokenized
        .filter(col("sitting_date") === lit(year))
        .groupBy("sitting_date", "member_gender").agg(flatten(collect_list("words")).alias("speeches_words"))
        .withColumn("speeches_len", size(col("speeches_words")))
        .orderBy("member_gender","sitting_date")
      gender_speeches_len.show()
    }

    val gender_count = tokenized
      .groupBy("sitting_date", "member_gender")
      .agg(array_distinct(collect_list("member_name")).alias("names"))
      .withColumn("names_count", size(col("names")))
      .orderBy("member_gender","sitting_date")
    gender_count.show(100)

    val males_speeches = tokenized
      .filter("member_gender = 'male'")
    //males_speeches.show()

    val males_speeches_vectorizer: CountVectorizerModel = new CountVectorizer()
      .setInputCol("words")
      .setOutputCol("raw_features")
      .setVocabSize(10000)
      .setMinDF(5)
      .fit(males_speeches)
    val males_speeches_countVectors = males_speeches_vectorizer.transform(males_speeches)

    val males_speeches_idf = new IDF()
      .setInputCol("raw_features")
      .setOutputCol("features")
      .fit(males_speeches_countVectors)
      .transform(males_speeches_countVectors)

    print("Type the number of males and females topics: ")
    val numTopics = scala.io.StdIn.readInt()

    println("Males Topics")

    val males_lda = new LDA()
      .setK(numTopics)
      .setMaxIter(3)
      .fit(males_speeches_idf)

    // Describe males topics.
    val males_topics = males_lda.describeTopics(10)

    // User-defined function that converts the weights of each term into the word corresponding to the vocabulary
    val males_term_to_voc_word = udf((arr:mutable.WrappedArray[Int]) => {
      arr.toArray.map(term => males_speeches_vectorizer.vocabulary(term))
    })

    val males_topics_terms = males_topics.withColumn("terms", males_term_to_voc_word(col("termIndices"))).select("topic", "terms")
    males_topics_terms.show(numTopics,false)


    val females_speeches = tokenized
      .filter("member_gender = 'female'")

    val females_speeches_vectorizer: CountVectorizerModel = new CountVectorizer()
      .setInputCol("words")
      .setOutputCol("raw_features")
      .setVocabSize(10000)
      .setMinDF(5)
      .fit(females_speeches)
    val females_speeches_countVectors = females_speeches_vectorizer.transform(females_speeches)

    val females_speeches_idf = new IDF()
      .setInputCol("raw_features")
      .setOutputCol("features")
      .fit(females_speeches_countVectors)
      .transform(females_speeches_countVectors)

    println("Females Topics")

    val females_lda = new LDA()
      .setK(numTopics)
      .setMaxIter(3)
      .fit(females_speeches_idf)

    // Describe males topics.
    val females_topics = females_lda.describeTopics(10)

    // User-defined function that converts the weights of each term into the word corresponding to the vocabulary
    val females_term_to_voc_word = udf((arr:mutable.WrappedArray[Int]) => {
      arr.toArray.map(term => females_speeches_vectorizer.vocabulary(term))
    })

    val females_topics_terms = females_topics.withColumn("terms", females_term_to_voc_word(col("termIndices"))).select("topic", "terms")
    females_topics_terms.show(numTopics,false)
  }
}
