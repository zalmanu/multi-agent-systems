package ro.uvt.mas.agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;

import javax.swing.*;

public class HostAgent extends Agent {
    protected void setup() {
        System.out.println("--- Starting the host agent ---");
        try {
            DFAgentDescription hostDescription = new DFAgentDescription();
            hostDescription.setName(getAID());
            DFService.register(this, hostDescription);
        } catch (FIPAException e) {
            System.out.println("An exception occurred in Host agent: " + e);
            e.printStackTrace();
        }
    }
}
