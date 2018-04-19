package ro.uvt.mas.agents;


import jade.core.Agent;
import jade.core.Service;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class GuestAgent extends Agent {
    protected void setup() {
        System.out.println("Starting guest: " + getLocalName());
        try {
            DFAgentDescription guestDescr = new DFAgentDescription();
            guestDescr.setName(getAID());

            ServiceDescription guestService = new ServiceDescription();
            guestService.setType("guest");
            guestService.setName("guest-service");

            guestDescr.addServices(guestService);
            DFService.register(this, guestDescr);
        } catch (FIPAException e) {
            System.out.println("An exeception occured in GuestAgent: " + e);
            e.printStackTrace();

        }
    }
}
