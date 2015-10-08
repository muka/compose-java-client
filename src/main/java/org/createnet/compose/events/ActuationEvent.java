/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.createnet.compose.events;

import org.createnet.compose.objects.Actuation;

/**
 *
 * @author Luca Capra <luca.capra@create-net.org>
 */
public class ActuationEvent {

    private final Actuation actuation;
    private String status;

    public ActuationEvent(Actuation actuation, String status) {
        this.actuation = actuation;
        this.status = status;
    }

    public Actuation getActuation() {
        return actuation;
    }

    public String getStatus() {
        return status;
    }

}
