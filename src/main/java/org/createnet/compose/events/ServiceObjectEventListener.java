/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.createnet.compose.events;

import org.createnet.compose.exception.ClientException;

/**
 *
 * @author Luca Capra <luca.capra@create-net.org>
 */
public interface ServiceObjectEventListener {

    public void onCreate(ServiceObjectEvent ev) throws ClientException;

    public void onUpdate(ServiceObjectEvent ev) throws ClientException;

    public void onDelete(ServiceObjectEvent ev) throws ClientException;

    public void onLoad(ServiceObjectEvent ev) throws ClientException;

}
