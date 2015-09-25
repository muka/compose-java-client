/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.createnet.compose.events;

import org.createnet.compose.data.ResultSet;
import org.createnet.compose.object.Stream;

/**
 *
 * @author Luca Capra <luca.capra@create-net.org>
 */
public class StreamEvent {

    private final Stream stream;
    private ResultSet resultset;

    public StreamEvent(Stream stream) {
        this.stream = stream;
    }
    
    public StreamEvent(Stream stream, ResultSet resultset) {
        this.stream = stream;
        this.resultset = resultset;
    }

    public Stream getStream() {
        return stream;
    }

    public ResultSet getResultset() {
        return resultset;
    }

}
