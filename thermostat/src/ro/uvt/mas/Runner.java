package ro.uvt.mas;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import ro.uvt.mas.agents.Environment;
import ro.uvt.mas.agents.Thermostat;

public class Runner {

    public static void main(String[] args) throws StaleProxyException {
        Properties properties = new Properties();
        Profile profile = new ProfileImpl(properties);
        AgentContainer agentContainer =  Runtime.instance().createMainContainer(profile);

        agentContainer.acceptNewAgent(Environment.NAME, new Environment()).start();
        agentContainer.acceptNewAgent(Thermostat.NAME, new Thermostat()).start();
    }
}
