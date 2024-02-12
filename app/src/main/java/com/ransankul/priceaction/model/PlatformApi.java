package com.ransankul.priceaction.model;

import java.util.Date;

public class PlatformApi {

    private Long id;
    private String platform;
    private String redirecturl;
    private boolean onOrOff;
    private boolean connectedOrNot;
    private String apikey;
    private String apisecret;
    private String apiname;
    private String jwttoken;
    private Date lastTokenTime;


    public PlatformApi() {
    }

    public PlatformApi(Long id, String platform, String redirecturl, boolean onOrOff, boolean connectedOrNot,
                       String apikey, String apisecret, String apiname, String jwttoken, Date lastTokenTime) {
        this.id = id;
        this.platform = platform;
        this.redirecturl = redirecturl;
        this.onOrOff = onOrOff;
        this.connectedOrNot = connectedOrNot;
        this.apikey = apikey;
        this.apisecret = apisecret;
        this.apiname = apiname;
        this.jwttoken = jwttoken;
        this.lastTokenTime = lastTokenTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isOnOrOff() {
        return onOrOff;
    }

    public void setOnOrOff(boolean onOrOff) {
        this.onOrOff = onOrOff;
    }

    public boolean isConnectedOrNot() {
        return connectedOrNot;
    }

    public void setConnectedOrNot(boolean connectedOrNot) {
        this.connectedOrNot = connectedOrNot;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getApisecret() {
        return apisecret;
    }

    public void setApisecret(String apisecret) {
        this.apisecret = apisecret;
    }

    public String getApiname() {
        return apiname;
    }

    public void setApiname(String apiname) {
        this.apiname = apiname;
    }

    public String getJwttoken() {
        return jwttoken;
    }

    public void setJwttoken(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public Date getLastTokenTime() {
        return lastTokenTime;
    }

    public void setLastTokenTime(Date lastTokenTime) {
        this.lastTokenTime = lastTokenTime;
    }

    public String getredirecturl() {
        return redirecturl;
    }

    public void setredirecturl(String redirecturl) {
        this.redirecturl = redirecturl;
    }
}
