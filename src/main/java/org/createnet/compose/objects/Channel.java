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
package org.createnet.compose.objects;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class Channel extends StreamContainer
{
    
    public String name;
    public String type;
    public String unit;

    public Channel() {
    }
    
    public Channel(JsonNode json) {
        name = json.get("name").asText();
        type = json.get("type").asText();
        unit = json.get("unit").asText();
    }

    @Override
    public void validate() throws ValidationException {
        throw new ValidationException("Not supported yet.");
    }

    @Override
    public void parse(String json) throws ParserException {
        try {
            parse(mapper.readTree(json));
        } catch (IOException ex) {
            throw new ParserException(ex);
        }
    }
    
    public void parse(JsonNode json) throws ParserException {
        name = json.get("name").asText();
        type = json.get("type").asText();
        unit = json.get("unit").asText();
    }
    
}
