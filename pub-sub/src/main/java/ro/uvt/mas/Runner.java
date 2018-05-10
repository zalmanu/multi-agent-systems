package ro.uvt.mas;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import ro.uvt.mas.agents.Broker;
import ro.uvt.mas.agents.Publisher;
import ro.uvt.mas.agents.Subscriber;


public class Runner {
    private static final int PUBLISHERS_CNT = 5;
    private static final int SUBSCRIBERS_CNT = 10;


    public static void main(String[] args) throws StaleProxyException {
        Properties properties = new Properties();
        Profile profile = new ProfileImpl(properties);
        AgentContainer agentContainer = Runtime.instance().createMainContainer(profile);

        agentContainer.acceptNewAgent("Broker", new Broker()).start();

        for (int i = 0; i < PUBLISHERS_CNT; i++) {
            agentContainer.acceptNewAgent("pub" + i, new Publisher()).start();
        }

        for (int i = 0; i < SUBSCRIBERS_CNT; i++) {
            agentContainer.acceptNewAgent("sub" + i, new Subscriber()).start();
        }
    }
}
