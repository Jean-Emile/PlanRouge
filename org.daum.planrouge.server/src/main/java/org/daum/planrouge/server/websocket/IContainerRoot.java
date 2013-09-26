package org.daum.planrouge.server.websocket;

import org.kevoree.planrouge.Agent;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Intervention;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 16/09/13
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public interface IContainerRoot {

    public ContainerRoot getContainer();

    public Agent getAgent(String id);

    public Intervention getIntervention(String id);

}
