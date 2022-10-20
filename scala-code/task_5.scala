package com.BigData.spark

import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.feature.Word2Vec
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.sql.functions._

class task_5 {
  def run(tokenized:DataFrame) {
    val clusters_dataset = tokenized
      .filter(col("sitting_date") >= lit(2009) && col("sitting_date") <= lit(2019))
      .groupBy("member_name", "political_party").agg(flatten(collect_list("words")).alias("speeches"))

    // Create a dataframe that includes the word2Vec vectors for each speech
    // The Word2Vec model gets as an input the words column and returns as an output the word2Vec_features column
    val word2Vec_result = new Word2Vec()
      .setInputCol("speeches")
      .setOutputCol("word2Vec_features")
      .setVectorSize(2)
      .setMinCount(0)
      .fit(clusters_dataset)
      .transform(clusters_dataset)

    // Spliting data into training and test sets
    val splits = word2Vec_result.randomSplit(Array(0.7, 0.3), seed = 42)
    val train_data = splits(0)
    val test_data = splits(1)

    // Create the KMeans model with num_clusters clusters
    val num_clusters = 3
    val kmeansModel = new KMeans()
      .setK(num_clusters)
      .setFeaturesCol("word2Vec_features")
      .setPredictionCol("prediction")
      .fit(train_data)

    // Create the dataframe with predictions
    val predictionsDF = kmeansModel.transform(test_data)

    // Check each member's participation in each cluster, as well as the participation of each party in the cluster
    val parties_clustering = predictionsDF.select("member_name", "political_party", "prediction").distinct().orderBy("prediction")
    parties_clustering.show(false)
    //    parties_clustering.coalesce(1).write.csv("/home/ozzy/IdeaProjects/BigData/src/data.csv")

    // Total number of speeches on each cluster in descending order
    predictionsDF.groupBy("prediction").count().orderBy(col("count").desc).show()
  }
}