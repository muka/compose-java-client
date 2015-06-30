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
import java.util.ArrayList;
import org.createnet.compose.client.RestClient;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class Stream extends ServiceObjectContainer {
    
    org.slf4j.Logger logger = LoggerFactory.getLogger(Stream.class);    
    
    public String name;
    public String type;
    public String description;
    
    public ArrayList<Channel> channels;
    
    public Stream(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(json);
        parse(tree);
    }

    Stream(JsonNode json) {
        parse(json);
    }

    protected void parse(JsonNode json) {
        
//        name = json.getObject().get("name").toString();
//        type  = json.getObject().get("type").toString();
//        description = json.getObject().get("description").toString();
        
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
