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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.createnet.compose.client.IClient;
import org.createnet.compose.data.IRecord;
import org.createnet.compose.data.RecordSet;
import org.createnet.compose.data.ResultSet;
import org.createnet.compose.events.StreamEvent;
import org.createnet.compose.exception.ClientException;
import org.createnet.compose.exception.RecordsetException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
@JsonSerialize(using = StreamSerializer.class)
public class Stream extends StreamContainer {
    
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

 
    /**
     *
     * @param dataset
     * @throws RequestException
     * @throws ClientException
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public void push(Map dataset) throws ClientException {
        push(dataset, new Date());
    }
    
    /**
     *
     * @param dataset
     * @param lastUpdate
     * @throws RequestException
     * @throws ClientException
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public void push(Map<String, Object> dataset, Date lastUpdate) throws ClientException {
        
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
     * @throws RequestException
     * @throws ClientException
     */
    public void push(ArrayList<IRecord> dataset) throws ClientException {
        push(dataset, new Date());
    }

    /**
     *
     * @param dataset
     * @param lastUpdate
     * @throws RequestException
     * @throws ClientException
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public void push(ArrayList<IRecord> dataset, Date lastUpdate) throws ClientException {
        RecordSet records = new RecordSet(dataset, lastUpdate);
        push(records);
    }
        
    public void push(RecordSet records) throws ClientException {      
        
        IClient.Subject subj = new IClient.Subject(this);
        try {
            subj.setContent(records.toJSON());       
        } catch (JsonProcessingException ex) {
            throw new ClientException(ex);
        }
        
        this.getClient().update(subj);    
    }
    
    public ResultSet pull() throws ClientException {
        return pull(false);
    }

    public ResultSet pull(boolean lastUpdate) throws ClientException {

        IClient.Subject subj = new IClient.Subject(this);
        subj.getExtras().put("lastUpdate", lastUpdate);
        ResultSet resultset;
        try {
            IClient.Result res = this.getClient().load(subj);        
            resultset = new ResultSet(this, res.getContent());
        } catch (RecordsetException ex) {
            throw new ClientException(ex);
        }
        
        if(hasListener()) getListener().onPull(new StreamEvent(this, resultset));
        
        return resultset;
    }

    
}
