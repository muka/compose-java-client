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
package org.createnet.compose.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.createnet.compose.exception.RecordsetException;
import org.createnet.compose.object.Channel;
import org.createnet.compose.object.Stream;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class RecordSet {

    private Date lastUpdate;
    private ArrayList<IRecord> records;

    public RecordSet(Stream stream, JsonNode row) throws RecordsetException {
        
        this.records = new ArrayList<>();
        this.lastUpdate = new Date();
        
        parseJson(stream, row);
    }
    
    public RecordSet(Stream stream, String body) throws IOException, RecordsetException {
        
        this.records = new ArrayList<>();        
        this.lastUpdate = new Date();
        
        ObjectMapper mapper = new ObjectMapper();
        parseJson(stream, mapper.readTree(body));
    }
    
    public RecordSet(ArrayList<IRecord> records) {
        this.records = records;
        this.lastUpdate = new Date();
    }

    public RecordSet(ArrayList<IRecord> records, Date date) {
        this.records = records;
        this.lastUpdate = date;
    }
    
    public static IRecord createRecord(Stream stream, String key, Object value) {

        if (!stream.channels.isEmpty() && !stream.channels.containsKey(key)) {
            return null;
        }
        
        Channel channel = stream.channels.get(key);
        IRecord record = null;

        if(channel == null) {

            String strVal = value.toString();
            if(strVal.equals("false") || strVal.equals("true")) {
                record = new BooleanRecord();
            }
//                else if(
//                    // matches [\d.\d,\d.\d]
//                    strVal.matches("\\[?\\w*-?\\d+(\\.\\d+)?\\w*\\,\\w*-?\\d+(\\.\\d+)?\\w*\\]?") ||
//                    // matches { "lat|lon
//                    (strVal.matches("\\w*\\{\\w*\\\"lat") || strVal.matches("\\w*\\{\\w*\\\"lon"))
//                    // ! wont match geohashes!
//                        
//                ) {
//                    record = new GeoPointRecord();
//                }
            else {

                NumberFormat formatter = NumberFormat.getInstance();
                ParsePosition pos = new ParsePosition(0);
                formatter.parse(strVal, pos);
                if(strVal.length() == pos.getIndex()) {
                    record = new NumberRecord();
                }
                else {
                    // defaults to string
                    record = new StringRecord();
                }
            }
            
            channel= new Channel();
            channel.setStream(stream);
            channel.name = key;
            
        }
        else {
            switch (channel.type) {
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
            }
        }

        record.setValue(value);
        record.setChannel(channel);

        return record;
    }

    public String toJSON() throws JsonProcessingException {
        
        if(records == null) {
            return null;
        }
        
        Map<String, Object> channels = new HashMap<>();
        for (IRecord record : records) {
            
            Map<String, Object> currValue = new HashMap<>();
            currValue.put("current-value", record.getValue());
            
            channels.put(record.getName(), currValue);
        }

        Map<String, Object> obj = new HashMap<>();
        obj.put("channels", channels);
        
        obj.put("lastUpdate", getLastUpdateTime());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public Date getLastUpdate() {
        if(lastUpdate == null) {
            setLastUpdate(new Date());
        }
        return lastUpdate;
    }

    public Long getLastUpdateTime() {
        return (Long) (getLastUpdate().getTime() / 1000);
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

    private void parseJson(Stream stream, JsonNode row) throws RecordsetException {
        
        if(row.has("lastUpdate")) {
            Date date = new Date(row.get("lastUpdate").asLong());
            this.setLastUpdate(date);
        }
        
        JsonNode channels = row.get("channels");
        
        if(channels == null) {
            throw new RecordsetException("`channels` property is missing");
        }
        
        for (Iterator<Map.Entry<String, JsonNode>> iterator = channels.fields(); iterator.hasNext();) {
            
            Map.Entry<String, JsonNode> item = iterator.next();
            
            String channelName = item.getKey();
            JsonNode nodeValue = item.getValue();
            
            
            // allow short-hand without current-value
            JsonNode valObj = nodeValue;
            if(nodeValue.has("current-value")) {
                valObj = nodeValue.get("current-value");
            }
            
            if(stream.channels != null && !stream.channels.isEmpty()) {
                if (stream.channels.containsKey(channelName)) {
                    this.addRecord(stream, channelName, valObj.asText());
                }
            }
            else {
                // definition is unknown, add all channels to the record set
                this.addRecord(stream, channelName, valObj.asText());
            }
            
        }

    }

    protected IRecord addRecord(Stream stream, String channelName, Object value) {

        IRecord record = RecordSet.createRecord(stream, channelName, value);
        if(record != null) {
            record.setRecordSet(this);
            this.records.add(record);
        }

        return record;
    }
    
    /**
     * @param channelName 
     * @return IRecord
     */
    public IRecord getByChannelName(String channelName) {
        for (IRecord record : records) {
            if(channelName.equals(record.getName())) {
                return record;
            }
        }
        return null;
    }
    
    /**
     * @param channel
     * @return IRecord
     */
    public IRecord getByChannel(Channel channel) {
        return getByChannelName(channel.name);
    }

}
