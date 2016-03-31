package actors

import akka.actor._
//import net.liftweb.json._
import spray.json._
import DefaultJsonProtocol._ // if you don't supply your own Protocol (see below)
import messages._

/**
  * Cleans up JSON payload sent by [[DataFetchActor]] and extracts [[Tweet]].
  * Filters out rate limit messages from the Twitter API.
  *
  * @param processor Actor that processes the tweets into a hashtag graph
  */
class CleanupActor(val processor: ActorRef) extends ActorBase {
  def receive = {
    // throw away rate-limiting messages
    case x:String if x.toString.startsWith("{\"limit\":") => println("rate limit! ignoring\n")

    case CountYourChildren => CountMyChildren

    // otherwise clean it and send on for processing
    case x:String => cleanAndSend(x)
  }


  def cleanAndSend(json: String) = {
    // Mon Mar 28 23:23:12 +0000 2016
    // return timestamp & hashtags
    // TODO: actually clean up JSON & extract
    println(s"cleaning up $json")
    processor ! json
  }

  //def parseTweet(json: String) = {
  //  //    implicit val formats = DefaultFormats
  //
  //
  //  // extract created at and convert into long
  //  // extract hashtags and add to list
  //
  //  val jsonString =
  //    """
  //          {"created_at":"Mon Mar 28 23:23:12 +0000 2016","id":714593712530042880,"id_str":"714593712530042880","text":"Want to work at Stanford Health Care? We're #hiring in #PaloAlto, CA! Click for details: https:\/\/t.co\/vm7oUVMwlK #Healthcare #Job #Jobs","source":"\u003ca href=\"http:\/\/www.tweetmyjobs.com\" rel=\"nofollow\"\u003eTweetMyJOBS\u003c\/a\u003e","truncated":false,"in_reply_to_status_id":null,"in_reply_to_status_id_str":null,"in_reply_to_user_id":null,"in_reply_to_user_id_str":null,"in_reply_to_screen_name":null,"user":{"id":120915363,"id_str":"120915363","name":"TMJ-CAP Health Jobs","screen_name":"tmj_CAP_health","location":"The Peninsula, CA","url":"http:\/\/www.careerarc.com\/job-seeker","description":"Follow this account for geo-targeted Healthcare job tweets in The Peninsula, CA. Need help? Tweet us at @CareerArc!","protected":false,"verified":false,"followers_count":372,"friends_count":279,"listed_count":48,"favourites_count":0,"statuses_count":301,"created_at":"Mon Mar 08 00:04:07 +0000 2010","utc_offset":-18000,"time_zone":"Quito","geo_enabled":true,"lang":"en","contributors_enabled":false,"is_translator":false,"profile_background_color":"253956","profile_background_image_url":"http:\/\/pbs.twimg.com\/profile_background_images\/315373982\/Twitter-BG_2_bg-image.jpg","profile_background_image_url_https":"https:\/\/pbs.twimg.com\/profile_background_images\/315373982\/Twitter-BG_2_bg-image.jpg","profile_background_tile":false,"profile_link_color":"4A913C","profile_sidebar_border_color":"000000","profile_sidebar_fill_color":"407DB0","profile_text_color":"000000","profile_use_background_image":true,"profile_image_url":"http:\/\/pbs.twimg.com\/profile_images\/707557079645425664\/do8XX1F1_normal.jpg","profile_image_url_https":"https:\/\/pbs.twimg.com\/profile_images\/707557079645425664\/do8XX1F1_normal.jpg","profile_banner_url":"https:\/\/pbs.twimg.com\/profile_banners\/120915363\/1457529731","default_profile":false,"default_profile_image":false,"following":null,"follow_request_sent":null,"notifications":null},"geo":{"type":"Point","coordinates":[37.4418834,-122.1430195]},"coordinates":{"type":"Point","coordinates":[-122.1430195,37.4418834]},"place":{"id":"3ad0f706b3fa62a8","url":"https:\/\/api.twitter.com\/1.1\/geo\/id\/3ad0f706b3fa62a8.json","place_type":"city","name":"Palo Alto","full_name":"Palo Alto, CA","country_code":"US","country":"United States","bounding_box":{"type":"Polygon","coordinates":[[[-122.190523,37.362824],[-122.190523,37.465918],[-122.097537,37.465918],[-122.097537,37.362824]]]},"attributes":{}},"contributors":null,"is_quote_status":false,"retweet_count":0,"favorite_count":0,"entities":{"hashtags":[{"text":"hiring","indices":[44,51]},{"text":"PaloAlto","indices":[55,64]},{"text":"Healthcare","indices":[113,124]},{"text":"Job","indices":[125,129]},{"text":"Jobs","indices":[130,135]}],"urls":[{"url":"https:\/\/t.co\/vm7oUVMwlK","expanded_url":"http:\/\/bit.ly\/1QNdBCL","display_url":"bit.ly\/1QNdBCL","indices":[89,112]}],"user_mentions":[],"symbols":[]},"favorited":false,"retweeted":false,"possibly_sensitive":false,"filter_level":"low","lang":"en","timestamp_ms":"1459207392233"}
  //    """.stripMargin
  //
  //
  //  // extract hashtags from entities
  //  // extract
  //
  //
  //
  //
  //  // parse
  //
  //  val jsonAst = jsonString.parseJson
  //
  //
  //  // parse out the hashtags from entities key
  //}


  //object MyJsonProtocol extends DefaultJsonProtocol {
  //  implicit val colorFormat = jsonFormat(Tweet, "created_at")
  //}
  //
  //import MyJsonProtocol._





}


