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
package org.createnet.compose.objects;

import org.createnet.compose.objects.serializer.ActuationSerializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
@JsonSerialize(using = ActuationSerializer.class)
public class Actuation extends ServiceObjectContainer {
       
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
    
    protected void parse(JsonNode json) {
        
        if(json.has("id")) {
            id = json.get("id").asText();
        }

        if(json.has("status")) {
            status = json.get("status").asText();
        }
        
        name = json.get("name").asText();
        description = json.get("description").asText();
    }

    protected void parse(JsonNode json, ServiceObject object) {
        this.setServiceObject(object);
        parse(json);
    }
    
    @Override
    public void validate() throws ValidationException {
        throw new ValidationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void parse(String json) throws ParserException {
        try {
            parse(mapper.readTree(json));
        } catch (IOException ex) {
            throw new ParserException(ex);
        }
    }
        
}
