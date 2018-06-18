package ro.uvt.mas.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.MessageQueue;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Thermostat extends Agent {
    public static String NAME = "thermostat";

    private static int temperature;

    protected void setup() {
        System.out.println("Starting the thermostat");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage message = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));

                if (message != null) {
                    int temp = Integer.parseInt(message.getContent());

                    if (temp > 18 && temp < 22) {
                        System.out.println("All good: 18 < temp < 22");
                    } else if(temp <= 18) {
                        System.out.println("Temp below limit: " + temp + ". INC-ing it.");
                        sendMsgToEnviron("inc");
                    } else {
                        System.out.println("Temp above limit: " + temp + ". DEC-ing it.");
                        sendMsgToEnviron("dec");
                    }
                } else {
                    block();
                }
            }
        });

        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            public void onTick() {
                sendMsgToEnviron("get");
            }
        });

    }

    private void sendMsgToEnviron(String content) {
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setContent(content);
        message.addReceiver(new AID(Environment.NAME, AID.ISLOCALNAME));
        send(message);
    }
}
