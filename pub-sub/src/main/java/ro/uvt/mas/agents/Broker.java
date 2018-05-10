package ro.uvt.mas.agents;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ro.uvt.mas.Message;
import ro.uvt.mas.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Broker extends Agent {
    private Map<Topic, List<AID>> subscribers = new HashMap<>();
    private Gson gson = new Gson();

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate pubTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                MessageTemplate subTemplate = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);

                ACLMessage pubMessage = receive(pubTemplate);
                ACLMessage subMessage = receive(subTemplate);

                if(subMessage != null) {
                    Topic topic = Topic.valueOf(subMessage.getContent());
                    AID aid = subMessage.getSender();

                    List<AID> subscribedToTopic = subscribers.getOrDefault(topic, new ArrayList<>());
                    subscribedToTopic.add(aid);
                    subscribers.put(topic, subscribedToTopic);
                }

                if (pubMessage != null) {
                    String jsonMessage = pubMessage.getContent();
                    Message message = gson.fromJson(jsonMessage, Message.class);
                    List<AID> subscribedToTopic = subscribers.getOrDefault(message.getTopic(), new ArrayList<>());

                    for(AID subscriber : subscribedToTopic) {
                        System.out.println("Broker - Sending " + message.getContent() + " to " + subscriber.getLocalName() + " on topic " + message.getTopic());
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.addReceiver(subscriber);
                        msg.setContent(Integer.toString(message.getContent()));
                        send(msg);
                    }
                }

                if (subMessage == null && pubMessage == null) {
                    block();
                }
            }
        });
    }
}
