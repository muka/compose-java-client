/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.createnet.compose.events;

/**
 *
 * @author Luca Capra <luca.capra@create-net.org>
 */
abstract class ActuationEventListener {
    abstract public void onStatus(ActuationEvent ev);
}
