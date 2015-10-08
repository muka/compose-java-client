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

import com.fasterxml.jackson.core.JsonProcessingException;
import org.createnet.compose.objects.serializer.StreamSerializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
@JsonSerialize(using = StreamSerializer.class)
public class Stream extends ServiceObjectContainer {
    
    org.slf4j.Logger logger = LoggerFactory.getLogger(Stream.class);    
    
    public String name;
    public String type;
    public String description;
    
    public Map<String, Channel> channels;
    
    public Stream(String json, ServiceObject object) throws IOException {
        initialize();
        JsonNode tree = mapper.readTree(json);
        parse(tree, object);
    }

    public Stream(JsonNode json, ServiceObject object) {
        initialize();
        parse(json, object);
    }
    
    public Stream(String json) throws IOException {
        initialize();
        JsonNode tree = mapper.readTree(json);
        parse(tree, null);
    }

    public Stream(JsonNode json) {
        initialize();
        parse(json, null);
    }
    
    public Stream() {
        initialize();
    }
    
    protected void initialize() {
        channels = new HashMap<>();
    }
    
    protected void parse(JsonNode json, ServiceObject object) {
        this.setServiceObject(object);
        parse(json);
    }
    
    protected void parse(JsonNode json) {
        
        name = json.get("name").asText();
        type = json.get("type").asText();
        description = json.get("description").asText();
        
        if(json.has("channels")) {
        
            for (JsonNode jsonChannel : json.get("channels")) {
                Channel channel = new Channel(jsonChannel);
                channel.setStream(this);
                channels.put(channel.name, channel);
            }
        }
        
    }
    
    @Override
    public void parse(String json) throws ParserException {
        try {
            parse(mapper.readTree(json));
        } catch (IOException ex) {
            throw new ParserException(ex);
        }
    }
    
    @Override
    public void validate() throws ValidationException {
        throw new ValidationException("Not implemented yet."); 
    }
    
}
