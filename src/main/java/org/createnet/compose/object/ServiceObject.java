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

import java.io.IOException;
import org.createnet.compose.events.ServiceObjectEvent;
import org.createnet.compose.events.ServiceObjectEventListener;
import org.createnet.compose.exception.RequestException;
import org.createnet.compose.exception.ClientException;
import org.createnet.compose.objects.client.IClient;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class ServiceObject extends org.createnet.compose.objects.ServiceObject
{
    
    protected ServiceObjectEventListener listener;

    public ServiceObject() {}
    public ServiceObject(String id) {
        super(id);
    }
    
    public ServiceObject create() throws RequestException, ClientException {
        if(hasListener()) getListener().onCreate(new ServiceObjectEvent(this));
        this.getClient().create(new IClient.Subject(this));
        return this;
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
    
    public void update() throws RequestException, ClientException {
        if(hasListener()) getListener().onUpdate(new ServiceObjectEvent(this));
        IClient.Result res = this.getClient().update(new IClient.Subject(this));
    }

    public ServiceObjectEventListener getListener() {
        return listener;
    }

    public void setListener(ServiceObjectEventListener listener) {
        this.listener = listener;
    }

    protected boolean hasListener() {
        return getListener() != null;
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
