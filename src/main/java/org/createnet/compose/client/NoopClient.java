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

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class NoopClient implements org.createnet.compose.objects.client.IClient {

    protected String apiKey;
    protected String uri = "http://servioticy.com";

    public NoopClient() {
    }

    @Override
    public void open(Config config) {}

    @Override
    public void close() {}

    @Override
    public Result create(Subject subj) {
        return new Result();
    }

    @Override
    public Result update(Subject subj) {
        return new Result();
    }

    @Override
    public Result load(Subject subj) {
        return new Result();
    }

    @Override
    public Result delete(Subject subj) {
        return new Result();
    }

    @Override
    public Result list(Subject subj) {
        return new Result();
    }

    @Override
    public Result status(Subject subj, String payload, Map extras) {
        return new Result();
    }

}
