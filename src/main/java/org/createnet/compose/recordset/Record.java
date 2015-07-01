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

import java.util.Date;
import org.createnet.compose.object.Channel;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
abstract public class Record<E> implements IRecord<E> {
    
    protected Channel channel;
    protected E value;
    protected Date lastUpdate;

    @Override
    abstract public String getType();
    
    @Override
    abstract public E parseValue(Object raw);
    
    @Override
    abstract public E getValue();
    
    @Override
    abstract public void setValue(Object value);

    @Override
    public String getName() {
        return channel != null ? channel.name : null;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }
    
    @Override
    public void  setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public Date getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
}
