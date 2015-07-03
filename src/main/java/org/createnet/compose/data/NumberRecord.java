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

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */   
public class NumberRecord extends Record<Long> {
    
    protected Long value;
    
    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getType() {
        return "number";
    }

    @Override
    public void setValue(Object value) {
        Long n = parseValue(value);
        this.value = n;
    }

    @Override
    public Long parseValue(Object value) {
        
        if(value instanceof String){
            return Long.parseLong((String)value);
        }
        
        return (Long)value;
    }    
    
}

