package ro.uvt.mas.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import ro.uvt.mas.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import static ro.uvt.mas.Messages.INTRO;

public class HostAgent extends Agent {
    public static String HOST_NAME = "host";
    private List<AID> guests;
    private int arrivedGuests;
    private boolean isThePartyOver = false;

    protected void setup() {
        System.out.println("--- Starting the host agent ---");
        try {
            DFAgentDescription hostDescription = new DFAgentDescription();
            hostDescription.setName(getAID());
            DFService.register(this, hostDescription);

            inviteGuests(10);

            addBehaviour(new CyclicBehaviour(this) {
                @Override
                public void action() {
                    ACLMessage message = receive();
                    if (message != null) {
                        if (message.getContent().equals(Messages.HELLO)) {
                            System.out.println("A new guest has arrived.");
                            arrivedGuests++;

                            if(arrivedGuests == guests.size()) {
                                System.out.println("All guests have arrived. Starting the party.");
                                startTheFun();
                            }
                        }
                    } else {
                        block();
                    }
                }
            });

        } catch (FIPAException e) {
            System.out.println("An exception occurred in Host agent: " + e);
            e.printStackTrace();
        }
    }

    private void inviteGuests(int cnt) {
        guests = new ArrayList();
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

    private void startTheFun() {
        System.out.println("Let the fun begin");

        sendMessage(ACLMessage.INFORM, Messages.RUMOUR, getRandomGuest(null));
    }

    private void introduceGuest(AID guest) {
        if(!isThePartyOver) {
            AID otherGuest = getRandomGuest(guest);
            sendMessage(ACLMessage.INFORM, INTRO + guest.getName(), otherGuest);
        }
    }

    private AID getRandomGuest(AID differentThan) {
        AID selected;

        do {
            selected = guests.get((new Random()).nextInt(guests.size()));
        } while(selected != null && selected.equals(differentThan));

        return selected;
    }

    private void sendMessage(int type, String messageContent, AID agent) {
        ACLMessage message = new ACLMessage(type);
        message.setContent(messageContent);
        message.addReceiver(agent);
        send(message);
    }
}

