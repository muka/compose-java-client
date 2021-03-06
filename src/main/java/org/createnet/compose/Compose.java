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

package org.createnet.compose;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.createnet.compose.client.IClient;
import org.createnet.compose.objects.ServiceObject;
import org.createnet.compose.client.RestClient;
import org.createnet.compose.exception.RequestException;
import org.createnet.compose.exception.ClientException;
import org.createnet.compose.objects.Actuation;
import org.createnet.compose.objects.Channel;
import org.createnet.compose.objects.ComposeComponent;
import org.createnet.compose.objects.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class Compose implements ComposeComponent {

    Logger logger = LoggerFactory.getLogger(Compose.class);
    
    protected final ObjectMapper mapper = new ObjectMapper();
    
    protected String uri = "http://servioticy.com";
    protected String apiKey = null;
    protected IClient client;

    public ServiceObject readDefinition(String json) throws ClientException {
        
        ServiceObject serviceObject = new ServiceObject();
        try {
            serviceObject.parse(json);
        } catch (ComposeComponent.ParserException ex) {
            throw new ClientException(ex);
        }
        
        serviceObject.setContainer(this);
        return serviceObject;
    }
    
    public ServiceObject load(String soid) throws ClientException {
        return (new ServiceObject(soid)).load();
    }
    
    public void delete(String soid) throws ClientException {
        (new ServiceObject(soid)).delete();
    }
    
    public void update(String soid, String definition) throws ClientException {
        ServiceObject so = readDefinition(definition);
        so.id = soid;
        so.update();
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getUri() {
        return uri;
    }
    
    @Override    
    public void setClient(IClient client) {
        this.client = client;
    }
    
    @Override
    public IClient getClient() {
        return this.client;
    }    
    
    public Channel createChannel(String soId, String streamName, String channelName) {
        Stream stream = createStream(soId, streamName);
        return createChannel(stream, channelName);
    }
    
    public Channel createChannel(Stream stream, String channelName) {

        Channel channel = new Channel();
        channel.setStream(stream);

        return channel;
    }
    
    public Stream createStream(ServiceObject so, String streamName) {
    
        Stream stream = new Stream();
        stream.name = streamName;
    
        stream.setServiceObject(so);
        stream.setContainer(so.getContainer());
        
        return stream;
    }
    
    public Stream createStream(String soId, String streamName) {
        ServiceObject so = getServiceObject(soId);
        return createStream(so, streamName);
    }
    
    public Actuation createActuation(ServiceObject so, String actuationName) {
    
        Actuation actuation = new Actuation();
        actuation.name = actuationName;
    
        actuation.setServiceObject(so);
        actuation.setContainer(so.getContainer());

        return actuation;
    }
    
    public Actuation createActuation(String soId, String actName) {
        ServiceObject so = getServiceObject(soId);
        return createActuation(so, actName);
    }

    public ServiceObject getServiceObject() {
        return getServiceObject(null);
    }
    
    public ServiceObject getServiceObject(String soid) {
        ServiceObject serviceObject = new ServiceObject(soid);
        serviceObject.setContainer(this);
        return serviceObject;
    }

    public ServiceObject createServiceObject(String soId) throws ClientException, RequestException {
        ServiceObject serviceObject = new ServiceObject(soId);
        serviceObject.setContainer(this);
        return createServiceObject(serviceObject);
    }
    
    public ServiceObject createServiceObject(ServiceObject serviceObject) throws ClientException, RequestException {
        
        IClient.Result res = this.getClient().create(new IClient.Subject(serviceObject));
        String json = res.getContent();
        
        JsonNode tree;
        try {
            tree = mapper.readTree(json);
        } catch (IOException ex) {
            throw new ClientException(ex);
        }
        
        serviceObject.id = tree.get("id").toString();
        serviceObject.createdAt = tree.get("createdAt").asLong();

        return serviceObject;
    }
    
    public static void main(String[] args) throws RequestException, ClientException, IOException, ParserException {
        
        Logger logger2 = LoggerFactory.getLogger(Compose.class);
        
        String apiKey = "Bearer f22cac2b27c16d79f1cf199ad81dbf2a9232524c";
        String uri = "http://servioticy.local";
        String soid = "1435572526142849de35ba51e46939f62d5b1f28d71a1";

        Compose compose = new Compose();
        compose.setClient(new RestClient(apiKey, uri));
        
        ServiceObject so = compose.load(soid);
        
        logger2.info(so.toJSON());
        
        ServiceObject so2 = compose.readDefinition(so.toJSON());
        
        logger2.info(so2.toJSON());
        
    }
    
}
