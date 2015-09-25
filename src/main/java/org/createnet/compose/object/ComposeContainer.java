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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.createnet.compose.Compose;
import org.createnet.compose.client.IClient;
import org.createnet.compose.exception.ClientException;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
abstract class ComposeContainer {
    
    protected final ObjectMapper mapper = new ObjectMapper();    
    
    @JsonBackReference
    protected Map<String, Object> extras = new HashMap<>();

    @JsonBackReference
    protected Compose container;

    public Compose getContainer() {
        return container;
    }

    public void setContainer(Compose container) {
        this.container = container;
    }
    
    @JsonBackReference
    public IClient getClient() {
        return getContainer().getClient();
    }
    
    public void setClient(IClient client) {
        getContainer().setClient(client);
    }
    
    public Map getExtras() {
        return extras;
    }
    
    public void addExtra(String key, Object val) {
        extras.put(key, val);
    }

    public ObjectNode toJsonNode() {
        ObjectNode node = mapper.convertValue(this, ObjectNode.class);
        node.putAll(getExtras());
        return node;
    }
    
    public String toJSON() throws ClientException {
        String json = null;
        try {
            json = mapper.writeValueAsString(this.toJsonNode());
        } catch (JsonProcessingException ex) {
            throw new ClientException(ex);
        }
        return json;
    }
    
}
