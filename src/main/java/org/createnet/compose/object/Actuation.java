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

import org.createnet.compose.objects.serializer.ActuationSerializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.createnet.compose.events.ActuationEvent;
import org.createnet.compose.events.ActuationEventListener;
import org.createnet.compose.exception.RequestException;
import org.createnet.compose.exception.ClientException;
import org.createnet.compose.objects.client.IClient;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
@JsonSerialize(using = ActuationSerializer.class)
public class Actuation extends org.createnet.compose.objects.Actuation {
       
    protected ActuationEventListener listener;
    
    public String status() throws IOException {
        
        IClient.Result result = this.getContainer().getClient().load(new IClient.Subject(this));
        String response = result.getContent();
        
        JsonNode json = mapper.readTree(response);
        String status = json.get("status").toString();
        
        if(hasListener()) getListener().onStatus(new ActuationEvent(this, status));
         
       return status;
    }
    
    public String status(String payload) throws RequestException, ClientException, IOException {
        
        Map<String, String> headers = new HashMap<>();
        
        headers.put("Content-Type", "text/plain");
        headers.put("accept", "text/plain");
        
        IClient.Subject subj = new IClient.Subject(this);
        
        IClient.Result res = this.getContainer().getClient().status(subj, payload, headers);
        String response = res.getContent();
        
        JsonNode json = mapper.readTree(response);
        String status = json.get("status").toString();

        if(hasListener()) getListener().onStatus(new ActuationEvent(this, status));
        
        return status;
    }

    public ActuationEventListener getListener() {
        return listener;
    }

    public void setListener(ActuationEventListener listener) {
        this.listener = listener;
    }

    protected boolean hasListener() {
        return getListener() != null;
    }

    @Override
    public void validate() throws ValidationException {
        throw new ValidationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void parse(String json) throws ParserException {
        try {
            parse(mapper.readTree(json));
        } catch (IOException ex) {
            throw new ParserException(ex);
        }
    }
        
}
