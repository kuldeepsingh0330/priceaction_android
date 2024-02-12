package com.ransankul.priceaction.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constants {

    private static final String API_BASE_URL = "http://192.168.158.64:8080";
    public static final String SHARED_PREFS_NAME = "ransankulpriceaction";
    public static final String KEY_STRING_VALUE = "JWTToken";
    public static final String LOGIN_USER_URL = API_BASE_URL+"/api/auth/login";
    public static final String REGISTER_USER_URL = API_BASE_URL+"/api/auth/register";

    public static final String LOAD_USERDETAILS_URL = API_BASE_URL+"/api/auth/user";

    public static final String USERAPIMAPPING_LOADALLAPI_URL= API_BASE_URL+"/api/userApiMappings/";
    public static final String USERAPIMAPPING_LOADPLATFORMJWTTOKENAPI_URL= API_BASE_URL+"/api/userApiMappings/platformJwtToken";
    public static final String USERAPIMAPPING_LOADALLPLATFORM_URL= API_BASE_URL+"/api/platform/";
    public static final String USERAPIMAPPING_SEARCHAPI_URL= API_BASE_URL+"/api/userApiMappings/search";
    public static final String USERAPIMAPPING_ADD_URL= API_BASE_URL+"/api/userApiMappings/add";

    public static final String USERAPIMAPPING_REMOVE_URL = API_BASE_URL+"/api/userApiMappings/remove/";

    public static final String USERAPIMAPPING_ONOFF_URL = API_BASE_URL+"/api/userApiMappings/onoff/";

    public static final String USERAPIMAPPING_CONNECTEDORDIS_URL = API_BASE_URL+"/api/userApiMappings/connectOrDis/";
    public static final String SEARCH_INSTRUMENT_KEY = API_BASE_URL+"/api/instrumentkey/search/";
    public static final String LOAD_ORDER_HISTORY_URL = API_BASE_URL+"/api/orderhistory/";
    public static final String ADD_WATCHLIST_URL = API_BASE_URL+"/api/Watchlist/add/";
    public static final String REMOVE_WATCHLIST_URL = API_BASE_URL+"/api/Watchlist/delete/";
    public static final String WEB_HOOK_URL = API_BASE_URL+"/webhook/buysell/";
    public static final String LOAD_ALL_RELATED_NEWS_URL = API_BASE_URL+"/api/relatednews/";

    public static final String UPSTOX_LOGIN_DIALOG_URL = "https://api-v2.upstox.com/login/authorization/dialog";
    public static final String LOCAL_UPSTOX_ACCESS_TOKEN_URL = API_BASE_URL+"/api/upstox/auth/gettoken";
    public static final String UPSTOX_MARKET_FEED_WEBSOCKET_URL = "https://api-v2.upstox.com/feed/market-data-feed/authorize";
    public static final String TRADING_VIEW_LOAD_CHART_URL = "https://www.tradingview.com/chart/?symbol=";


    public static String getTokenValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.KEY_STRING_VALUE, "");
    }
}
