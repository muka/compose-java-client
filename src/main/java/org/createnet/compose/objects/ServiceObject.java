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

import org.createnet.compose.objects.serializer.ServiceObjectSerializer;
import org.createnet.compose.objects.deserializer.ServiceObjectDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    public String description = "";
    
    public Long createdAt;
    public Long updatedAt;
    
    public Map<String, Object> customFields;
    public Map<String, String> properties;

    public Map<String, Stream> streams;
    public Map<String, Subscription> subscriptions;
    public Map<String, Actuation> actuations;
    

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
        this.actuations = new HashMap<>();
        
        createdAt = System.currentTimeMillis();
        updatedAt = System.currentTimeMillis();
        
        addExtra("public", true);
    }
         
    @Override
    public String toString() {
        return "ServiceObject<"+ this.id +">";
    }
    
    public String getId() {
        return id;
    }

    @Override
    public void validate() throws ValidationException {
        throw new ValidationException("Not supported yet.");
    }

    @Override
    public void parse(String json) throws ParserException {
        
        ServiceObject serviceObject;
        try {
            serviceObject = mapper.readValue(json, ServiceObject.class);
        } catch (IOException ex) {
            throw new ParserException(ex);
        }
        
        id = serviceObject.id;
        name = serviceObject.name;
        description = serviceObject.description;
        customFields = serviceObject.customFields;
        properties = serviceObject.properties;
        createdAt = serviceObject.createdAt;
        updatedAt = serviceObject.updatedAt;
        streams = serviceObject.streams;
        subscriptions = serviceObject.subscriptions;
        actuations = serviceObject.actuations;

        serviceObject = null;
                
    }
    
}
