/*
 * Copyright 2014 CREATE-NET <http://create-net.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.createnet.compose.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class RestClient {

    public class HttpException extends Exception {

        public int status;
        public String statusText;
        public String body;

        HttpException(int status, String statusText, String body) {
            this.status = status;
            this.statusText = statusText;
            this.body = body;
        }

        @Override
        public String toString() {
            return "HTTP " + this.status + " - " + this.statusText;
        }
    }

    Logger logger = LoggerFactory.getLogger(RestClient.class);

    protected String apiKey;
    protected String uri = "http://servioticy.com";

    public RestClient(String apiKey) {
        this.apiKey = apiKey;
        this.setup();
    }

    public RestClient(String apiKey, String uri) {

        this.apiKey = apiKey;
        this.uri = uri;

        this.setup();
    }

    private void setup() {
        Unirest.setDefaultHeader("Authorization", apiKey);
        Unirest.setDefaultHeader("accept", "application/json");
    }

    protected String getBody(HttpResponse<String> response) throws HttpException {

        if (response.getStatus() >= 400) {
            throw new HttpException(response.getStatus(), response.getStatusText(), response.getBody());
        }

        return response.getBody();
    }

    public String post(String path, String body) throws UnirestException, HttpException {
        try {
            return getBody(Unirest.post(this.uri + path).body(body).asString());
        } catch (UnirestException ex) {
            logger.error("Client exception", ex);
            throw ex;
        } catch (HttpException ex) {
            logger.error("Error during request", ex);
            throw ex;
        }
    }

    public String put(String path, String body) throws UnirestException, HttpException {
        try {
            return getBody(Unirest.put(this.uri + path).body(body).asString());
        } catch (UnirestException ex) {
            logger.error("Error during request", ex);
            throw ex;
        }
    }

    public String delete(String path) throws UnirestException, HttpException {
        try {
            return getBody(Unirest.delete(this.uri + path).asString());
        } catch (UnirestException ex) {
            logger.error("Error during request", ex);
            throw ex;
        }
    }

    public String get(String path) throws UnirestException, HttpException {
        try {
            return getBody(Unirest.get(this.uri + path).asString());
        } catch (UnirestException ex) {
            logger.error("Error during request", ex);
            throw ex;
        }
    }

    public void close() {
        try {
            Unirest.shutdown();
        } catch (IOException ex) {
            logger.error("Error during shutdown", ex);
        }
    }
}
