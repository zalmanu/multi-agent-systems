package ro.uvt.mas.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import java.util.Vector;

public class HostAgent extends Agent {
    private Vector<AID> guests;
    protected void setup() {
        System.out.println("--- Starting the host agent ---");
        try {
            DFAgentDescription hostDescription = new DFAgentDescription();
            hostDescription.setName(getAID());
            DFService.register(this, hostDescription);

            inviteGuests(10);

        } catch (FIPAException e) {
            System.out.println("An exception occurred in Host agent: " + e);
            e.printStackTrace();
        }
    }

    private void inviteGuests(int cnt) {
        guests = new Vector<>();
        PlatformController platformController = getContainerController();
        for(int i = 1; i <= cnt; i++) {
            try {
                String name = "guest-" + i;
                AgentController guest = platformController.createNewAgent(name, "ro.uvt.mas.agents.GuestAgent", null);
                guest.start();
                guests.add(new AID(name, AID.ISLOCALNAME));
            } catch (ControllerException e) {
                System.out.println("An exception occurred while inviting guests: " + e);
                e.printStackTrace();
            }

        }
    }
}
