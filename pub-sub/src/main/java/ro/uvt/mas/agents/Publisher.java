package ro.uvt.mas.agents;


import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ro.uvt.mas.Message;
import ro.uvt.mas.Topic;

public class Publisher extends Agent {
    private static final int PUB_INTERVAL = 1000;
    private Gson gson = new Gson();

    @Override
    protected void setup() {
        System.out.println("Starting publisher: " + getLocalName());

        addBehaviour(new TickerBehaviour(this, PUB_INTERVAL) {
            @Override
            public void onTick() {
                Topic topic = Topic.getRandomTopic();
                int content = (int) (Math.random() * 100);

                Message message = new Message(topic, content);
                String jsonMessage = gson.toJson(message);

                ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
                aclMessage.setContent(jsonMessage);
                aclMessage.addReceiver(new AID("Broker", AID.ISLOCALNAME));
                send(aclMessage);
            }
        });

    }
}
