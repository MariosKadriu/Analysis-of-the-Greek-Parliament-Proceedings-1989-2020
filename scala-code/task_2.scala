package com.BigData.spark

import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel, IDF}
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

class task_2 {
  def run(spark:SparkSession, tokenized:DataFrame){
    import spark.implicits._

    // User-defined function that transforms the "features" column's vectors to dense vectors
    // in a new column called dense features
    val asDense = udf((v: Vector) => v.toDense)

    val task2_dataset = tokenized
        .filter(col("sitting_date") === lit(2019))
        .limit(10000)
        .groupBy("member_name").agg(flatten(collect_list("words")).alias("speeches"))

    val task2_vectorizer: CountVectorizerModel = new CountVectorizer()
      .setInputCol("speeches")
      .setOutputCol("raw_features")
      .setVocabSize(10000)
      .setMinDF(5)
      .fit(task2_dataset)
    val task2_countVectors = task2_vectorizer.transform(task2_dataset)

    val task2_results = new IDF()
      .setInputCol("raw_features")
      .setOutputCol("features")
      .fit(task2_countVectors)
      .transform(task2_countVectors)

    val newDf = task2_results
      .withColumn("dense_features", asDense($"features"))

    // Cosine similarity between two vectors is calculated using calc_CosineSimilarity function
    val calc_CosineSimilarity = udf {(elem_1: Vector, elem_2:Vector) =>
      val vector_1 = elem_1.toArray
      val vector_2 = elem_2.toArray

      val l1 = scala.math.sqrt(vector_1.map(x => x * x).sum)
      val l2 = scala.math.sqrt(vector_2.map(x => x * x).sum)

      val scalar = vector_1.zip(vector_2).map(p => p._1 * p._2).sum
      scalar / (l1 * l2)
    }

    // Create a replica of the newDf dataset named filtered_df with column names member_name_frd, dense_frd, speech_frd
    // to join it with newDf dataset to calculate and store the cosine similarity between two members on a new column
    // named cosine_similarity as well as their rank
    val filtered_df = newDf
      .select('member_name.alias("member_name_frd"), 'dense_features.alias("dense_frd"))

    val joinedDf = newDf.join(broadcast(filtered_df), 'member_name =!= 'member_name_frd)
      .limit(10000)
      .withColumn("cosine_similarity", calc_CosineSimilarity(col("dense_frd"), col("dense_features")))
      .withColumn("cosine_similarity", when(col("cosine_similarity").isNaN, 0).otherwise(col("cosine_similarity")))
      .select("member_name","member_name_frd", "cosine_similarity")
      .orderBy($"cosine_similarity".desc)
    joinedDf.show(100,false)
  }
}
