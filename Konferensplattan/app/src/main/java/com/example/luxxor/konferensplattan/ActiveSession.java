package com.example.luxxor.konferensplattan;

/**
 * Created by luxxor on 2015-05-23.
 */
public class ActiveSession {
    public String name;
    public String sessionId;
    public String token;
    public String apiKey;

    @Override
    public String toString() {
        return "ActiveSession{" +
                "name='" + name + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", token='" + token + '\'' +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}
