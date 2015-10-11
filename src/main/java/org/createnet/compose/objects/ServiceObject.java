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

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.createnet.compose.objects.serializer.ServiceObjectSerializer;
import org.createnet.compose.objects.deserializer.ServiceObjectDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.createnet.compose.client.IClient;
import org.createnet.compose.events.ServiceObjectEvent;
import org.createnet.compose.exception.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
@JsonSerialize(using = ServiceObjectSerializer.class)
@JsonDeserialize(using = ServiceObjectDeserializer.class)
public class ServiceObject extends ServiceObjectContainer
{
    
    Logger logger = LoggerFactory.getLogger(ServiceObject.class);
    
    @JsonBackReference
    private boolean isNew = false;
    
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
        
        if(this.name == null)
            throw new ValidationException("name field missing");
        
        if(!this.isNew())
            throw new ValidationException("id field missing");
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
        
        isNew(id != null);
        
        serviceObject = null;
                
    }

    
     public ServiceObject load() throws ClientException {

         IClient.Result res = this.getClient().load(new IClient.Subject(this.id));
         String json = res.getContent();

         try {
             parse(json);
         } catch (ParserException ex) {
             throw new ClientException(ex);
         }
         
         if(hasListener()) getListener().onLoad(new ServiceObjectEvent(this));
         
         return this;
     }
     
     public void delete() throws ClientException {
         this.getClient().delete(new IClient.Subject(new ServiceObject(this.id)));
         this.id = null;
         if(hasListener()) getListener().onDelete(new ServiceObjectEvent(this));        
     }
     
     public void update() throws ClientException {
         if(hasListener()) getListener().onUpdate(new ServiceObjectEvent(this));
         IClient.Result res = this.getClient().update(new IClient.Subject(this));
     }

    public List<ServiceObject> list() throws ClientException {
        
        IClient.Result res = this.getClient().list(new IClient.Subject());
        
        List<ServiceObject> list = null;
        try {
            ServiceObject[] array = mapper.readValue(res.getContent(), ServiceObject[].class);
            list = Arrays.asList(array);
        } catch (IOException ex) {
            throw new ClientException(ex);
        }

        if(hasListener()) getListener().onList(new ServiceObjectEvent(list));
        
        return list;
    }

    public boolean isNew() {
        return isNew;
    }
    
    public boolean isNew(boolean isnew) {
        isNew = isnew;
        return isNew;
    }
    
}
