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

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.createnet.compose.events.ServiceObjectEventListener;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
abstract class ServiceObjectContainer extends ComposeContainer {

    @JsonBackReference
    protected ServiceObjectEventListener listener;
    
    @JsonBackReference
    protected ServiceObject serviceObject;

    @Override
    public ComposeComponent getContainer() {
        return getServiceObject().getContainer();
    }
    
    public void setServiceObject(ServiceObject serviceObject) {
        this.serviceObject = serviceObject;
        this.setContainer(serviceObject.getContainer());
    }

    public ServiceObject getServiceObject() {
        return serviceObject;
    }

    @Override
    public ServiceObjectEventListener getListener() {
        return listener;
    }

    public void setListener(ServiceObjectEventListener listener) {
        this.listener = listener;
    }    
    
    @Override
    abstract public void validate() throws ValidationException;

    @Override
    abstract public void parse(String json) throws ParserException;
    
}
