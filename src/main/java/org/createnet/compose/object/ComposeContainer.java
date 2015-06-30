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

import org.createnet.compose.client.RestClient;

import org.createnet.compose.Compose;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
abstract class ComposeContainer {
    
    protected Compose container;

    public Compose getContainer() {
        return container;
    }

    public void setContainer(Compose container) {
        this.container = container;
    }
    
    public RestClient getClient() {
        return getContainer().getClient();
    }
    
}
