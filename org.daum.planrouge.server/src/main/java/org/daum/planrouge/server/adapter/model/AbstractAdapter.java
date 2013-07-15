package org.daum.planrouge.server.adapter.model;

import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.PlanrougeFactory;
import org.kevoree.planrouge.factory.MainFactory;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 15/07/13
 * Time: 10:41
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAdapter implements IAdapter {

    private PlanrougeFactory modelfactory = null;

    public PlanrougeFactory getModelfactory() {
        return modelfactory;
    }

    public void setModelfactory(PlanrougeFactory modelfactory) {
        this.modelfactory = modelfactory;
    }


}
