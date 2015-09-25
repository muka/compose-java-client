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
package org.createnet.compose.client;

import java.util.Map;
import org.createnet.compose.exception.ClientException;
import org.createnet.compose.exception.RequestException;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public interface IClient {

    public void open();

    public void close();

    public String post(String path, String body, Map<String, String> headers) throws ClientException, RequestException;

    public String post(String path, String body) throws ClientException, RequestException;

    public String put(String path, String body) throws ClientException, RequestException;

    public String put(String path, String body, Map<String, String> headers) throws ClientException, RequestException;

    public String delete(String path) throws ClientException, RequestException;

    public String get(String path) throws ClientException, RequestException;

}
