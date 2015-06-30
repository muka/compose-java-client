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
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.createnet.compose.recordset.BooleanRecord;
import org.createnet.compose.recordset.GeoPointRecord;
import org.createnet.compose.recordset.IRecord;
import org.createnet.compose.recordset.NumberRecord;
import org.createnet.compose.recordset.StringRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class ResultSet extends ArrayList<IRecord> {
   
    Logger logger = LoggerFactory.getLogger(ResultSet.class);

    protected Stream stream;

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }
    
    public ResultSet(String jsonString) {

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

            for (Iterator<JsonNode> iterator = list.iterator(); iterator.hasNext();) {
                JsonNode row = iterator.next();

                JsonNode channels = row.get("channels");
                
                Date lastUpdate = new Date(row.get("lastUpdate").asLong());
                
                for (Map.Entry<String, Channel> entrySet : stream.channels.entrySet()) {
                    
                    String key = entrySet.getKey();
                    Channel channel = entrySet.getValue();
                    
                    if(channels.has(channel.name)) {
                        
                        IRecord record;
                        switch(channel.type) {
                            case "geo_point":
                                record = new GeoPointRecord(channels.get(channel.name).asText());
                            case "number":
                                record = new NumberRecord();
                                record.setValue(channels.get(channel.name).asLong());
                            case "boolean":
                                record = new BooleanRecord();
                                record.setValue(channels.get(channel.name).asBoolean());
                            case "string":
                            default:
                                record = new StringRecord();
                                record.setValue(channels.get(channel.name).asText());
                            break;
                        }
                        
                        record.setChannel(channel);
                        record.setLastUpdate(lastUpdate);
                        
                        this.add(record);
                    }
                }
                
            }

        }

    }

}
