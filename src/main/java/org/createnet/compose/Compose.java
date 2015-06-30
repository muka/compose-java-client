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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.logging.Level;
import org.createnet.compose.object.ServiceObject;
import org.createnet.compose.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class Compose {

    Logger logger = LoggerFactory.getLogger(Compose.class);
    
    protected String uri = "http://servioticy.com";
    protected String apiKey;
    protected RestClient client;
    
    public Compose(String apiKey, String uri) {
        this.apiKey = apiKey;
        this.uri = uri;
    }
    public Compose(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public ServiceObject load(String soid) {
        
        ServiceObject so = new ServiceObject();
        
        so.setContainer(this);
        so.id = soid;
        
        try {
            so.load();
        } catch (UnirestException | RestClient.HttpException ex) {
            logger.error("Error during request", ex);
        }
        return so;
    }

    public static void main(String[] args) {
        
        Logger logger2 = LoggerFactory.getLogger(Compose.class);
        
        String apiKey = "Bearer f22cac2b27c16d79f1cf199ad81dbf2a9232524c";
        String uri = "http://servioticy.local";
        String soid = "1435572526142849de35ba51e46939f62d5b1f28d71a1";

        Compose compose = new Compose(apiKey, uri);
        
        ServiceObject so = compose.load(soid);
        
        
        logger2.info(so.toJSON());
        logger2.info(so.toString());
        
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getUri() {
        return uri;
    }
    
    public RestClient getClient() {
        
        if(client == null) {
            client = new RestClient(apiKey, uri);
        }

        return client;
    }
    
    
}
