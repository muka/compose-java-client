/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.createnet.compose.client;

import java.util.Map;
import org.createnet.compose.objects.Actuation;
import org.createnet.compose.objects.ServiceObject;
import org.createnet.compose.objects.Stream;
import org.createnet.compose.objects.Subscription;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public interface IClient {
    
    public interface Config {
    }
    
    public class Subject {
        
        protected String content;
        protected ServiceObject serviceObject;
        protected Stream stream;
        protected Actuation actuation;
        protected Subscription subscription;
        protected Map extras;
        
        public Subject() {}
        public Subject(String content) {
            this.content = content;
        }
        public Subject(ServiceObject obj) {
            this.serviceObject = obj;
        }
        public Subject(Stream obj) {
            this.stream = obj;
        }
        public Subject(Actuation obj) {
            this.actuation = obj;
        }
        public Subject(Subscription obj) {
            this.subscription = obj;
        }

        public Map getExtras() {
            return extras;
        }

        public String getContent() {
            return content;
        }

        public ServiceObject getServiceObject() {
            return serviceObject;
        }

        public Stream getStream() {
            return stream;
        }

        public Actuation getActuation() {
            return actuation;
        }

        public Subscription getSubscription() {
            return subscription;
        }
        
        public boolean hasContent() {
            return content != null;
        }

        public boolean hasServiceObject() {
            return serviceObject != null;
        }

        public boolean hasStream() {
            return stream != null;
        }

        public boolean hasActuation() {
            return actuation != null;
        }

        public boolean hasSubscription() {
            return subscription != null;
        }

        public void setContent(String content) {
            this.content = content;
        }
        
    }
    
    public class Result extends Subject {
        
        Throwable err;
        
        public Result() {}        
        public Result(Throwable err) {
            this.err = err;
        }
        public Result(String content) {
            super(content);
        }
        public Result(ServiceObject obj) {
            super(obj);
        }
        public Result(Stream obj) {
            super(obj);
        }
        public Result(Actuation obj) {
            super(obj);
        }
        public Result(Subscription obj) {
            super(obj);
        }

        
    }
    
    public void open(Config config);
    public void close();
    public Result create(Subject subj);
    public Result update(Subject subj);
    public Result load(Subject subj);
    public Result delete(Subject subj);
    public Result list(Subject subj);
    public Result status(Subject subj, String payload, Map extras);    
}
