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
package org.createnet.compose.objects.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.createnet.compose.objects.ServiceObject;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class ServiceObjectSerializer extends JsonSerializer<ServiceObject> {

    @Override
    public void serialize(ServiceObject t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {

        jg.writeStartObject();
        
        jg.writeStringField("id", t.id);
        jg.writeStringField("name", t.name);
        jg.writeStringField("description", t.description);
        
        jg.writeObjectField("customFields", t.customFields);
        jg.writeObjectField("streams", t.streams);
        jg.writeObjectField("actuations", t.actuations);
        
        jg.writeEndObject();

    }
    
}
