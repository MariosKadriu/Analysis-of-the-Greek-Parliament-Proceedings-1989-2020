package com.BigData.spark

import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel, IDF, Tokenizer}

object BigData {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("BigData")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    val df = spark.read.format("csv").option("header","true").option("inferSchema", "true").load("/home/ozzy/IdeaProjects/BigData/src/finaltrue.csv")

    // Create a tokenizer instance that takes the column speech as an input and returns the column words as an output
    // The final output with the individual terms is stored into tokenized constant
    val tokenizer = new Tokenizer().setInputCol("speech").setOutputCol("words")
    val tokenized = tokenizer.transform(df)

    // Using the CountVectorizerModel instance, convert a collection of text documents into vectors of token counts.
    // The raw features column is the output of the column words, which is the input.
    // The transformation's final output is kept in the countVectors constant.
    val vectorizer: CountVectorizerModel = new CountVectorizer()
      .setInputCol("words")
      .setOutputCol("raw_features")
      .setVocabSize(10000)
      .setMinDF(5)
      .fit(tokenized)
    val countVectors = vectorizer.transform(tokenized)

    // Create an IDF instance that receives the raw_features column as an input and returns the column with the name features
    // as an output after the transformation into constant results
    val results = new IDF()
      .setInputCol("raw_features")
      .setOutputCol("features")
      .fit(countVectors)
      .transform(countVectors)


    /* TASK 1 */
    val task1 = new task_1()
    task1.run(tokenized, vectorizer, results)

    /* TASK 2 */
    val task2 = new task_2()
    task2.run(spark, tokenized)

    /* TASK 3 */
    val task3 = new task_3()
    task3.run(spark, tokenized)

    /* TASK 4 */
    val task4 = new task_4()
    task4.run(vectorizer, results)

    /* TASK 5 */
    val task5 = new task_5()
    task5.run(tokenized)

    /* TASK 6 */
    val task6 = new task_6()
    task6.run(tokenized)
  }
}