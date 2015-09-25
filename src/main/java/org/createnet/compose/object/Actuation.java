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
package org.createnet.compose.object;

import org.createnet.compose.serializer.ActuationSerializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.createnet.compose.events.ActuationEvent;
import org.createnet.compose.events.ActuationEventListener;
import org.createnet.compose.exception.RequestException;
import org.createnet.compose.exception.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
@JsonSerialize(using = ActuationSerializer.class)
public class Actuation extends ServiceObjectContainer {
    
    protected ActuationEventListener listener;
    
    Logger logger = LoggerFactory.getLogger(Actuation.class);    
    
    public String id = null;
    public String status = null;
    public String name;
    public String description;
    
    public Map<String, Channel> channels;
    
    public Actuation(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(json);
        parse(tree, null);
    }
    
    public Actuation(String json, ServiceObject object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(json);
        parse(tree, object);
    }

    public Actuation(JsonNode json, ServiceObject object) {
        parse(json, object);
    }
    
    public Actuation(JsonNode json) {
        parse(json, null);
    }
    
    public Actuation() {}
    
    protected void parse(JsonNode json, ServiceObject object) {
        
        this.setServiceObject(object);
        
        if(json.has("id")) {
            id = json.get("id").asText();
        }

        if(json.has("status")) {
            status = json.get("status").asText();
        }
        
        name = json.get("name").asText();
        description = json.get("description").asText();
    }
    
    public String status() throws RequestException, ClientException, IOException {
        
        String response = this.getContainer().getClient().get("/actuations/" + this.id);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);
        
        String status = json.get("status").toString();
        
        if(hasListener()) getListener().onStatus(new ActuationEvent(this, status));
        
        return status;
    }
    
    public String status(String payload) throws RequestException, ClientException, IOException {
        
        Map<String, String> headers = new HashMap<>();
        
        headers.put("Content-Type", "text/plain");
        headers.put("accept", "text/plain");
        
        String response = this.getContainer().getClient().put("/actuations/" + this.id, payload, headers);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);
        
        String status = json.get("status").toString();
        
        if(hasListener()) getListener().onStatus(new ActuationEvent(this, status));
        
        return status;
    }

    public ActuationEventListener getListener() {
        return listener;
    }

    public void setListener(ActuationEventListener listener) {
        this.listener = listener;
    }

    protected boolean hasListener() {
        return getListener() != null;
    }
        
}
