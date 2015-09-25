/*
 * Copyright 2015 Luca Capra <luca.capra@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.createnet.compose.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.Iterator;
import org.createnet.compose.object.Actuation;
import org.createnet.compose.object.ServiceObject;
import org.createnet.compose.object.Stream;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class ServiceObjectDeserializer extends JsonDeserializer<ServiceObject> {

    @Override
    public ServiceObject deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        
        ServiceObject serviceObject = new ServiceObject();
        
        JsonNode tree = jp.getCodec().readTree(jp);
  
        serviceObject.id = tree.get("id").asText();
        serviceObject.name = tree.get("name").asText();
        serviceObject.description = tree.get("description").asText();
        
        for (JsonNode jsonStream : tree.get("streams")) {
            Stream stream = new Stream(jsonStream);
            serviceObject.streams.put(stream.name, stream);
        }
        
        for (JsonNode json : tree.get("actuations")) {
            Actuation actuation = new Actuation(json);
            serviceObject.actuations.put(actuation.name, actuation);
        }
        
        return serviceObject;
    }
}
