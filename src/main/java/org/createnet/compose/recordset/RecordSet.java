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
package org.createnet.compose.recordset;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.createnet.compose.object.Stream;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class RecordSet {
    
    private Date lastUpdate;
    private ArrayList<IRecord> records;
    
    public RecordSet(ArrayList<IRecord> records) {
        this.records = records;
        this.lastUpdate = new Date();
    }
    
    public RecordSet(ArrayList<IRecord> records, Date date) {
        this.records = records;
        this.lastUpdate = date;
    }

    public static IRecord createRecord(Stream stream, String key, Object value) {
        
        if(!stream.channels.containsKey(key)) {
            return null;
        }
        
        IRecord record;
        switch(stream.channels.get(key).type) {
            case "string":
                record = new StringRecord();
                break;
            case "boolean":
                record = new BooleanRecord();
                break;
            case "number":
                record = new NumberRecord();
                break;
            case "geo_point":
                record = new GeoPointRecord();
                break;
            default:
                return null;
        }
        
        record.setValue(value);
        return record;
    }
    
    public String toJSON() {
        
        Map<String, Object> channels = new HashMap<>();
        
        for (IRecord record : records) {
            channels.put(record.getName(), record.getValue());
        }
        
        Map<String, Object> obj = new HashMap<>();
        obj.put("channels", channels);
        obj.put("lastUpdate", (Long)lastUpdate.getTime() / 1000);
        
        try {
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
            
        } catch (IOException e) {
            return null;
        }        

    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ArrayList<IRecord> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<IRecord> records) {
        this.records = records;
    }

    
    
}
