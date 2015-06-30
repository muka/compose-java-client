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

import org.createnet.compose.client.RestClient;
import org.createnet.compose.serializer.StreamSerializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
    
    public Stream(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(json);
        parse(tree);
    }

    Stream(JsonNode json) {
        parse(json);
    }

    protected void parse(JsonNode json) {
        
        channels = new HashMap<>();
        
        name = json.get("name").asText();
        type = json.get("type").asText();
        description = json.get("description").asText();
        
        if(json.has("channels")) {
        
            for (Iterator<JsonNode> iterator = json.get("channels").iterator(); iterator.hasNext();) {
                JsonNode jsonChannel = iterator.next();            

                Channel channel = new Channel(jsonChannel);
                channel.setStream(this);
                channels.put(channel.name, channel);

            }
        }
        
    }
    
    public void push() {
    }
    
    public ResultSet pull() throws RestClient.HttpException {
        return pull(false);
    }

    public ResultSet pull(boolean lastUpdate) throws RestClient.HttpException {
        
        
        String path = "/" + this.getServiceObject().id + "/" + this.name;
        try {
            
            String response = this.getContainer().getClient().get(path);
            
            return new ResultSet(response);

        } catch (UnirestException ex) {
            return null;
        }
       
    }

}
