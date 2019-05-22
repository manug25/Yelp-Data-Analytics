package com.manu.yelp.stream

import org.apache.spark.sql.types._

object SchemaProvider {

  val businessSchema1 = new StructType(
    Array(
      StructField("business_id", StringType, true),
      StructField("name", StringType, true),
      StructField("address", StringType, true),
      StructField("city",StringType,true),
      StructField("state",StringType, true),
      StructField("poastal_code", StringType, true),
      StructField("latitude", DoubleType, true),
      StructField("longitude", DoubleType,true),
      StructField("stars", FloatType, true),
      StructField("review_count", IntegerType, true),
      StructField("is_open", IntegerType, true))
  )


  val businessSchema = StructType(
    Array(
      StructField("business_id", StringType, true),
      StructField("name", StringType, true),
      StructField("address", StringType, true),
      StructField("city",StringType,true),
      StructField("state",StringType, true),
      StructField("poastal_code", StringType, true),
      StructField("latitude", DoubleType, true),
      StructField("longitude", DoubleType,true),
      StructField("stars", FloatType, true),
      StructField("review_count", IntegerType, true),
      StructField("is_open", IntegerType, true),
      StructField("attributes", StructType(Array(
        StructField("BusinessParking", StructType(Array(
          StructField("garage", BooleanType, true),
          StructField("street", BooleanType, true),
          StructField("validated", BooleanType, true),
          StructField("lot", BooleanType, true),
          StructField("valet", BooleanType, true)
        )), true),
        StructField("Ambience", StructType(Array(
          StructField("touristy", BooleanType, true),
          StructField("hipster", BooleanType, true),
          StructField("romantic", BooleanType, true),
          StructField("divey", BooleanType, true),
          StructField("intimate", BooleanType, true),
          StructField("trendy", BooleanType, true),
          StructField("upscale", BooleanType, true),
          StructField("classy", BooleanType, true),
          StructField("casual", BooleanType, true)
        )), true),
        StructField("Music", StructType(Array(
          StructField("dj", BooleanType, true),
          StructField("background_music", BooleanType, true),
          StructField("no_music", BooleanType, true),
          StructField("jukebox", BooleanType, true),
          StructField("live", BooleanType, true),
          StructField("video", BooleanType, true),
          StructField("karaoke", BooleanType, true)
        )), true),
        StructField("BikeParking", BooleanType, true),
        StructField("OutdoorSeating", BooleanType, true),
        StructField("GoodForKids", BooleanType, true),
        StructField("NoiseLevel", StringType, true),
        StructField("RestaurantsGoodForGroups", BooleanType, true),
        StructField("GoodForDancing", BooleanType, true),
        StructField("CoatCheck", BooleanType, true),
        StructField("WiFi", StringType, true),
        StructField("RestaurantsPriceRange2", StringType, true),
        StructField("HappyHour", BooleanType, true),
        StructField("Alcohol", StringType, true),
        StructField("ByAppointmentOnly", BooleanType, true),
        StructField("HasTV", BooleanType, true),
        StructField("RestaurantsReservations", BooleanType, true),
        StructField("BusinessAcceptsCreditCards", BooleanType, true),
        StructField("BestNights", StructType(Array(
          StructField("monday", BooleanType),
          StructField("tuesday", BooleanType),
          StructField("friday", BooleanType),
          StructField("wednesday", BooleanType),
          StructField("thursday", BooleanType),
          StructField("sunday", BooleanType),
          StructField("saturday", BooleanType)
        )
        ),true))), true),
      StructField("categories", StringType, true),
      StructField("hours", StructType(Array(
        StructField("Monday", StringType, true),
        StructField("Tuesday", StringType, true),
        StructField("Wednesday", StringType, true),
        StructField("Thursday", StringType, true),
        StructField("Friday", StringType, true),
        StructField("Saturday", StringType, true),
        StructField("Sunday", StringType, true),
      )), true)
    )
  )

  val userSchema = StructType(Array(
    StructField("user_id", StringType, true),
    StructField("name", StringType, true),
    StructField("review_count", IntegerType, true),
    StructField("yelping_since", StringType, true),
    StructField("friends", ArrayType(StringType), true),
    StructField("useful", IntegerType, true),
    StructField("funny", IntegerType, true),
    StructField("cool", IntegerType, true),
    StructField("fans", IntegerType, true),
    StructField("elite", ArrayType(IntegerType), true),
    StructField("average_stars", FloatType, true)
  ))

  val reviewSchema = StructType(Array(
    StructField("review_id", StringType, true),
    StructField("user_id", StringType, true),
    StructField("business_id", StringType, true),
    StructField("stars", IntegerType, true),
    StructField("date", StringType, true),
    StructField("text", StringType, true),
    StructField("useful", IntegerType, true),
    StructField("funny", IntegerType, true),
    StructField("cool", IntegerType, true)
  ))

  val checkinSchema = StructType(Array(
    StructField("business_id", StringType, true),
    StructField("date", StringType, true)
  ))

  val tipSchema = StructType(Array(
    StructField("text", StringType, true),
    StructField("date", StringType, true),
    StructField("compliment_count", StringType, true),
    StructField("business_id", StringType, true),
    StructField("user_id", StringType, true),
  ))

}
