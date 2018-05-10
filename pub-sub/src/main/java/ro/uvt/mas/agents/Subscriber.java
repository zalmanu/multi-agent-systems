package ro.uvt.mas.agents;

import com.sun.jmx.snmp.SnmpStatusException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ro.uvt.mas.Message;
import ro.uvt.mas.Topic;

public class Subscriber extends Agent {
    private static final int REPORT_INTERVAL = 10000;
    Message message = new Message();

    @Override
    protected void setup() {
        System.out.println("Starting subscriber: " + getLocalName());

        Topic topic = Topic.getRandomTopic();

        message.setTopic(topic);
        subscribeToTopic(topic);

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage pubMessage = receive();
                if (pubMessage != null) {
                    int content = Integer.parseInt(pubMessage.getContent());
                    message.setContent(content);
                } else {
                    block();
                }
            }
        });

        addBehaviour(new TickerBehaviour(this, REPORT_INTERVAL) {
            @Override
            protected void onTick() {
                System.out.println(getName() + " - last value: " + message.getContent());
            }
        });

    }

    private void subscribeToTopic(Topic topic) {
        System.out.println(getName() + " - Subscribing to: " + topic);
        ACLMessage aclMessage = new ACLMessage(ACLMessage.SUBSCRIBE);
        aclMessage.addReceiver(new AID("Broker", AID.ISLOCALNAME));
        aclMessage.setContent(topic.toString());
        send(aclMessage);
    }
}

