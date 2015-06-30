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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.createnet.compose.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class ServiceObject extends ComposeContainer implements Serializable {
    
    Logger logger = LoggerFactory.getLogger(ServiceObject.class);
    
    public String id;
    
    public String name;
    public String description;
    
    public Map<String, Object> customFields;
    public Map<String, String> properties;

    public Map<String, Stream> streams;
    public Map<String, Subscription> subscriptions;
//    public List<Actuation> actuations;

    public void parseDefinition(String raw) {
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree;
        try {
            tree = mapper.readTree(raw);
        } catch (IOException ex) {
            logger.error("Cannot parse definition " + raw, ex);
            return;
        }
        
        id = tree.get("id").asText();
        name = tree.get("name").asText();
        description = tree.get("description").asText();
        
        for (Iterator<JsonNode> iterator = tree.get("streams").iterator(); iterator.hasNext();) {
            JsonNode jsonStream = iterator.next();
            
            Stream stream = new Stream(jsonStream);
            streams.put(stream.name, stream);
        }
                
    }
    
    public void create() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }
    
    public void update() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }
    
    public ServiceObject load() throws UnirestException, RestClient.HttpException {
        
        String response = this.getClient().get("/" + id);
        parseDefinition(response);
        
        return this;
    }

    public void delete() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }
    
    @Override
    public String toString() {
        return "ServiceObject<"+ this.id +">";
    }
    
}
