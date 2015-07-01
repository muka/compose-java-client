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

import com.fasterxml.jackson.core.JsonProcessingException;
import org.createnet.compose.serializer.StreamSerializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.createnet.compose.exception.HttpException;
import org.createnet.compose.exception.RecordsetException;
import org.createnet.compose.exception.RestClientException;
import org.createnet.compose.recordset.IRecord;
import org.createnet.compose.recordset.RecordSet;

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
        initialize();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(json);
        parse(tree);
    }

    public Stream(JsonNode json) {
        initialize();
        parse(json);
    }
    
    public Stream() {
        initialize();
    }
    
    protected void initialize() {
        channels = new HashMap<>();
    }
    
    protected void parse(JsonNode json) {
        
        initialize();
        
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
    
    /**
     *
     * @param dataset
     * @throws HttpException
     * @throws RestClientException
     */
    public void push(HashMap<String, Object> dataset) throws HttpException, RestClientException {
        push(dataset);
    }
    
    /**
     *
     * @param dataset
     * @param lastUpdate
     * @throws HttpException
     * @throws RestClientException
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public void push(HashMap<String, Object> dataset, Date lastUpdate) throws HttpException, RestClientException, JsonProcessingException {
        
        ArrayList<IRecord> records = new ArrayList<>();
        
        for (Map.Entry<String, Object> entrySet : dataset.entrySet()) {
            
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            
            IRecord record = RecordSet.createRecord(this, key, value);
            
            if(record == null) continue;
            
            records.add(record);
        }
        
        push(records, lastUpdate);
    }
    
    /**
     *
     * @param dataset
     * @throws HttpException
     * @throws RestClientException
     */
    public void push(ArrayList<IRecord> dataset) throws HttpException, RestClientException, JsonProcessingException {
        push(dataset, new Date());
    }

    /**
     *
     * @param dataset
     * @param lastUpdate
     * @throws HttpException
     * @throws RestClientException
     */
    public void push(ArrayList<IRecord> dataset, Date lastUpdate) throws HttpException, RestClientException, JsonProcessingException {
        RecordSet records = new RecordSet(dataset, lastUpdate);
        push(records);
    }
        
    public void push(RecordSet records) throws HttpException, RestClientException, JsonProcessingException {
        
        String path = "/" + this.getServiceObject().id + "/streams/" + this.name;
        
        String json = records.toJSON();
        this.getContainer().getClient().put(path, json);
    }
    
    public ResultSet pull() throws HttpException, RestClientException, RecordsetException {
        return pull(false);
    }

    public ResultSet pull(boolean lastUpdate) throws HttpException, RestClientException, RecordsetException {

        String path = "/" + this.getServiceObject().id + "/streams/" + this.name + ( lastUpdate ? "/lastUpdate" : "" );
        String response = this.getContainer().getClient().get(path);

        return new ResultSet(this, response);
    }

}
