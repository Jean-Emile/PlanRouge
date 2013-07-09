package org.daum.planrouge.server;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 09/07/13
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "PlanRouge")
@ComponentType
public class ServerPlanRouge extends AbstractComponentType {

    @Start
    public void start() {

               // start server
    }

    @Stop
    public void stop() {


    }


    @Update
    public void update() {

           // TODO update webbit server port
    }
}
