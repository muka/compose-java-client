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

import org.createnet.compose.serializer.ServiceObjectSerializer;
import org.createnet.compose.deserializer.ServiceObjectDeserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.createnet.compose.exception.HttpException;
import org.createnet.compose.exception.RestClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
@JsonSerialize(using = ServiceObjectSerializer.class)
@JsonDeserialize(using = ServiceObjectDeserializer.class)
public class ServiceObject extends ComposeContainer
{
    
    Logger logger = LoggerFactory.getLogger(ServiceObject.class);
    
    public String id;
    
    public String name;
    public String description;
    
    public Map<String, Object> customFields;
    public Map<String, String> properties;

    public Map<String, Stream> streams;
    public Map<String, Subscription> subscriptions;
//    public Map<Actuation> actuations;

    public ServiceObject() {
        initialize();
    }
    
    public ServiceObject(String soid) {
        initialize();
        this.id = soid;
    }

    private void initialize() {

        this.customFields = new HashMap<>();
        this.properties = new HashMap<>();
        
        this.streams = new HashMap<>();
        this.subscriptions = new HashMap<>();
//        this.actuations = new HashMap<>();
    }

    public String toJSON() {
        
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            logger.error("Error converting to JSON", ex);
        }
        
        return json;
    }
    
    @Override
    public String toString() {
        return "ServiceObject<"+ this.id +">";
    }
    
    public ServiceObject load() throws HttpException, RestClientException, IOException {
        return this.getContainer().load(id);
    }
    
    public void delete() throws HttpException, RestClientException, IOException {
        this.getContainer().delete(id);
        this.id = null;
    }
    
    public void update() throws HttpException, RestClientException, IOException {
        this.getContainer().update(id, this.toJSON());
    }
    
}
