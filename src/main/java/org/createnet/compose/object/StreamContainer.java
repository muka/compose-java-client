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
package org.createnet.compose.object;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.createnet.compose.Compose;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
abstract class StreamContainer extends ServiceObjectContainer
{
    
    @JsonBackReference
    protected Stream stream;

    @Override
    public Compose getContainer() {
        return stream.getServiceObject().getContainer();
    }
    
    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }
    
}
