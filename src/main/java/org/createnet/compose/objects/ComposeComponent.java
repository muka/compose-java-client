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

import org.createnet.compose.client.IClient;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public interface ComposeComponent {
    
    public class ValidationException extends Exception {

        public ValidationException(String message, Throwable cause) {
            super(message, cause);
        }

        public ValidationException(String message) {
            super(message);
        }

        public ValidationException(Throwable cause) {
            super(cause);
        }
    
    }

    public class ParserException extends Exception {

        public ParserException(String message, Throwable cause) {
            super(message, cause);
        }

        public ParserException(String message) {
            super(message);
        }

        public ParserException(Throwable cause) {
            super(cause);
        }
    }
    
    public IClient getClient();
    public void setClient(IClient client);
    
}
