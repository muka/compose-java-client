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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import org.createnet.compose.exception.RecordsetException;
import org.createnet.compose.recordset.RecordSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class ResultSet extends ArrayList<RecordSet> {
   
    Logger logger = LoggerFactory.getLogger(ResultSet.class);

    protected Stream stream;

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }
    
    public ResultSet(Stream stream, String jsonString) throws RecordsetException {
        this.stream = stream;
        if(jsonString != null) {
            parse(jsonString);
        }
    }

    private void parse(String jsonString) throws RecordsetException {
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json;
        try {
            json = mapper.readTree(jsonString);
        } catch (IOException ex) {
            logger.error("Error parsing " + jsonString, ex);
            return;
        }

        JsonNode list = json.get("data");
        
        if (list.isArray()) {

            for (JsonNode row : list) {
                RecordSet recordset = new RecordSet(stream, row);
                this.add(recordset);
            }

        }
        
    }
    
}
