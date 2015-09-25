/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.createnet.compose.events;

import org.createnet.compose.object.ServiceObject;

/**
 *
 * @author Luca Capra <luca.capra@create-net.org>
 */
public class ServiceObjectEvent {

    private final ServiceObject object;

    public ServiceObjectEvent(ServiceObject object) {
        this.object = object;
    }

    public ServiceObject getObject() {
        return object;
    }

}
