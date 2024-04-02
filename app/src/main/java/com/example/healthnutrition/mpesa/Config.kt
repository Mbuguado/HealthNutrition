package com.example.healthnutrition.mpesa

import com.androidstudy.daraja.util.TransactionType

object Config {

    var CONSUMER_KEY = ""
    var CONSUMER_SECRET = ""
    var CALLBACK_URL = "http://mycallbackurl.com/checkout.php"
    var BUSINESS_SHORTCODE = "174379"
    var ACCOUNT_TYPE = TransactionType.CustomerBuyGoodsOnline
}