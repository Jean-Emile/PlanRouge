package org.daum.planrouge.server;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.kevoree.planrouge.Agent;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Intervention;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 23/07/13
 * Time: 17:24
 * To change this template use File | Settings | File Templates.
 */
public class test {
    public static void main( String[]args){
        // Adapter Factory + ContainerRoot
        AdapterFactory adapterFactory = AdapterFactory.getInstance();
        ContainerRoot containerRoot =   adapterFactory.getFactory().createContainerRoot();

        //intervention
        Intervention intervention = adapterFactory.getFactory().createIntervention();
        intervention.setId("111");
        intervention.setDescription("Explosion centrale nucleaire");

        // liste Agents
        List<Agent> listAgents = new LinkedList<Agent>();
                           //Agents
        Agent agent = adapterFactory.getFactory().createAgent();
        agent.setMatricule("1");

        Agent agent2 = adapterFactory.getFactory().createAgent();
        agent2.setMatricule("2222");
        Agent agent3 = adapterFactory.getFactory().createAgent();
        agent3.setMatricule("3");
        Agent agent4 = adapterFactory.getFactory().createAgent();
        agent4.setMatricule("4");

        //Ajout agents à la liste
        listAgents.add(agent);
        listAgents.add(agent2);
        listAgents.add(agent3);
        listAgents.add(agent4);

        // Ajouts des agents au containerRoot
        containerRoot.addAllAgents(listAgents);


        agent2.setIntervention(intervention);
        intervention.setAffecte(listAgents);

         containerRoot.addInterventions(intervention);
        System.out.println(containerRoot.findAgentsByID("2222").getIntervention().getId());
    }
}
