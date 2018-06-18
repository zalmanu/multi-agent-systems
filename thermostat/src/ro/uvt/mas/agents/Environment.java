package ro.uvt.mas.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Environment extends Agent {
    public static String NAME = "environ";

    private int temperature = 25;

    protected void setup() {
        System.out.println("Setting up the environ");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage message = receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

                if (message != null) {
                    if (message.getContent().startsWith("get")) {
                        provideTemperature();
                    } else if (message.getContent().startsWith("dec")) {
                        System.out.println("Got DEC msg.");
                        temperature -= 1;
                    } else if (message.getContent().startsWith("inc")) {
                        System.out.println("Got INC msg.");
                        temperature += 1;
                    }
                } else {
                    block();
                }
            }
        });

    }

    private void provideTemperature() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(Integer.toString(temperature));
        message.addReceiver(new AID(Thermostat.NAME, AID.ISLOCALNAME));
        send(message);
    }
}
