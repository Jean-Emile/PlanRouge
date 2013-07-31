import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONException;
import org.kevoree.planrouge.Agent;
import org.kevoree.planrouge.Intervention;
import org.kevoree.planrouge.Victime;
import org.webbitserver.WebSocketConnection;
import org.webbitserver.WebSocketHandler;
import org.webbitserver.netty.WebSocketClient;

import java.net.URI;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/07/13
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
public class BenchServer implements Runnable {


    private Thread t;
    private AdapterFactory adapterFactory = new AdapterFactory();
    private Intervention intervention;
    private Random r = new Random();
    private String url = "ws://192.168.1.101:8080/add";

    public BenchServer() {
        t = new Thread(this);
        intervention = adapterFactory.getFactory().createIntervention();
        intervention.setId("2");
        intervention.setDescription("accident 2");
        for(int i = 0 ; i<10 ; i++)          {
            Agent agent = adapterFactory.getFactory().createAgent();
            agent.setMatricule("t"+i);
            try {
                send(url, adapterFactory.build(agent).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            intervention.addAffecte(agent);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            send(url, adapterFactory.build(intervention).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

  //      t.start();

    }

    @Override
    public void run() {

        while (Thread.currentThread().isAlive()) {


            Victime v = adapterFactory.getFactory().createVictime();
            v.setNom("edef");
            v.setId("tag" + r.nextInt());
            intervention.addVictimes(v);
         //   v.setIntervention(intervention);
            try {

                send(url, adapterFactory.build(v).toString());
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }


    }


    public void send(String url, final String data) {

        WebSocketClient client = new WebSocketClient(URI.create(url), new WebSocketHandler() {
            @Override
            public void onOpen(WebSocketConnection webSocketConnection) throws Throwable {
                System.out.println("Send");
                webSocketConnection.send(data);

            }

            @Override
            public void onClose(WebSocketConnection webSocketConnection) throws Throwable {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onMessage(WebSocketConnection webSocketConnection, String s) throws Throwable {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onMessage(WebSocketConnection webSocketConnection, byte[] bytes) throws Throwable {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onPing(WebSocketConnection webSocketConnection, byte[] bytes) throws Throwable {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onPong(WebSocketConnection webSocketConnection, byte[] bytes) throws Throwable {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        client.start();
    }

    public static void main(String argv[]) {

        BenchServer t = new BenchServer();
    }
}
