package com.lisaxdevelopment.lisax;

import java.awt.*;
import java.util.Properties;

public class Config {

    private final String token;
    private final Color botColor = new Color(25, 25, 112);
    private final String mongoUrl;
    private final String fixerKey;
    private final String cloudVisionKey;

    Config(Properties properties) {
        token = properties.getProperty("token");
        fixerKey = properties.getProperty("fixerKey");
        mongoUrl = properties.getProperty("mongoURL");
        cloudVisionKey = properties.getProperty("cloudVisionKey");
        properties.clear();
    }

    public String getToken() {
        return token;
    }

    public Color getBotColor() {
        return botColor;
    }

    public String getMongoUrl() {
        return mongoUrl;
    }

    public String getFixerKey() {
        return fixerKey;
    }

    public String getCloudVisionKey() {
        return cloudVisionKey;
    }
}
