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
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.createnet.compose.exception.RequestException;
import org.createnet.compose.objects.ComposeComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class RestClient implements IClient {
    
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

    protected String getBody(HttpResponse<String> response) throws RequestException {

        if (response.getStatus() >= 400) {
            throw new RequestException(response.getStatus(), response.getStatusText(), response.getBody());
        }

        return response.getBody();
    }

    public String post(String path, String body, Map<String, String> headers) throws UnirestException, RequestException {

        HttpRequestWithBody req = Unirest.post(this.uri + path);

        if(headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                req.header(entry.getKey(), entry.getValue());
            }
        }

        return getBody(req.body(body).asString());
    }
    
    public String post(String path, String body) throws UnirestException, RequestException {
        return post(path, body, new HashMap<String, String>());
    }

    public String put(String path, String body) throws UnirestException, RequestException {
        return put(path, body, new HashMap<String, String>());
    }

    public String put(String path, String body, Map<String, String> headers) throws UnirestException, RequestException {
        
        HttpRequestWithBody req = Unirest.put(this.uri + path);

        if(headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                req.header(entry.getKey(), entry.getValue());
            }
        }

        return getBody(req.body(body).asString());

    }

    public String delete(String path) throws RequestException, UnirestException  {
        return getBody(Unirest.delete(this.uri + path).asString());
    }

    public String get(String path) throws RequestException, UnirestException {
        return getBody(Unirest.get(this.uri + path).asString());
    }

    private Result notImplemented() {
        return new Result(new Exception("Not Implemented"));
    }
    
    @Override
    public void close() {
        try {
            Unirest.shutdown();
        } catch (IOException ex) {
            logger.error("Error during shutdown", ex);
        }
    }

    @Override
    public void open(Config config) {
    }

    @Override
    public Result create(Subject subj) {
        
        if(subj.hasServiceObject()) {
            try {                
                String content = this.post("/", subj.getServiceObject().toJSON());
                return new IClient.Result(content);
            } catch (ComposeComponent.ParserException | RequestException | UnirestException ex) {
                return new Result(ex);
            }
        }
        
        return notImplemented();
    }

    @Override
    public Result update(Subject subj) {
        
        if(subj.hasServiceObject()) {
            try {
                String content = put("/" + subj.getServiceObject().id, subj.getServiceObject().toJSON());
                return new Result(content);
            } catch (ComposeComponent.ParserException | RequestException | UnirestException ex) {
                return new Result(ex);
            }
        }

        if(subj.hasStream()) {
            try {                
                
                String path = "/" + subj.getStream().getServiceObject().id + "/streams/" + subj.getStream().name;
                String json = subj.getContent();
                String content = put(path, json);
                
                return new Result(content);
            } catch (RequestException | UnirestException ex) {
                return new Result(ex);
            }
        }        
        
        return notImplemented();
    }

    @Override
    public Result load(Subject subj) {
        
        if(subj.hasServiceObject()) {
            try {
                String content = get("/" + subj.getServiceObject().id);
                return new Result(content);
            } catch (RequestException | UnirestException ex) {
                return new Result(ex);
            }
        }
        
        if(subj.hasStream()) {
            try {
                String path = "/" + subj.getStream().getServiceObject().id + "/streams/" + subj.getStream().name;
                if(subj.getExtras().containsKey("lastUpdate")) {
                    path += "lastUpdate";
                }
                String content = get(path);
                return new Result(content);
            } catch (RequestException | UnirestException ex) {
                return new Result(ex);
            }
        }
        
        if(subj.hasActuation()) {
            try {
                String content = get("/actuations/" + subj.getServiceObject().id);
                return new Result(content);
            } catch (RequestException | UnirestException ex) {
                return new Result(ex);
            }
        }
        
        return notImplemented();
    }

    @Override
    public Result delete(Subject subj) {
        
        if(subj.hasServiceObject()) {
            try {
                delete("/" + subj.getServiceObject().id);
                return new Result();
            } catch (RequestException | UnirestException ex) {
                return new Result(ex);
            }
        }
        
        return notImplemented();
    }

    @Override
    public Result list(Subject subj){
        
        if(subj.hasServiceObject()) {
            try {
                String content = get("/");
                return new Result(content);
            } catch (RequestException | UnirestException ex) {
                return new Result(ex);
            }
        }
        
        return notImplemented();
    }

    @Override
    public Result status(Subject subj, String payload, Map extras){
        
        if(subj.hasActuation()) {
            try {
                String content = put("/actuations/" + subj.getActuation().id, payload, extras);
                return new Result(content);
            } catch (RequestException | UnirestException ex) {
                return new Result(ex);
            }
        }

        return notImplemented();
    }
}
