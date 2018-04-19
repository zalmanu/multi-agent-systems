package ro.uvt.mas.agents;


import jade.core.AID;
import jade.core.Agent;
import jade.core.MessageQueue;
import jade.core.Service;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ro.uvt.mas.Messages;

public class GuestAgent extends Agent {
    private boolean rumourHeard = false;

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

            sayHelloToHost();

            addBehaviour(new CyclicBehaviour(this) {
                @Override
                public void action() {
                    ACLMessage message = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                    if (message != null) {
                        if(message.getContent().startsWith(Messages.RUMOUR)) {
                            handleRumour();
                        }
                    } else {
                        block();
                    }
                }
            });
        } catch (FIPAException e) {
            System.out.println("An exception occurred in GuestAgent: " + e);
            e.printStackTrace();

        }
    }

    private void sayHelloToHost() {
        sendMessage(ACLMessage.INFORM, Messages.HELLO, HostAgent.HOST_NAME);
    }

    private void handleRumour() {
        if(!rumourHeard) {
            System.out.println(getLocalName() + " received the rumour for the first time.");
            sendMessage(ACLMessage.INFORM, Messages.RUMOUR, HostAgent.HOST_NAME);
            rumourHeard = true;
        }
    }

    private void sendMessage(int type, String messageContent, String name) {
        ACLMessage message = new ACLMessage(type);
        message.setContent(messageContent);
        message.addReceiver(new AID(name, AID.ISLOCALNAME));
        send(message);
    }

    private void sendMessage(int type, String messageContent, AID agent) {
        ACLMessage message = new ACLMessage(type);
        message.setContent(messageContent);
        message.addReceiver(agent);
        send(message);
    }

}
