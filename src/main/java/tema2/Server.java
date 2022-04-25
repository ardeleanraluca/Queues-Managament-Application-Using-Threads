package tema2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;


public class Server implements Runnable {

    private BlockingQueue<Task> clients;
    private AtomicInteger waitingPeriod;

    public static float averageWaitingPeriod = 0;


    public Server() {
        clients = new LinkedBlockingDeque<>();
        waitingPeriod = new AtomicInteger();
    }

    public void addClient(Task client) {
        clients.add(client);
        waitingPeriod.getAndAdd(client.getServiceTime());

        averageWaitingPeriod += waitingPeriod.intValue();

    }

    @Override
    public void run() {
        String text = "";
        while (true) {

            Task c = clients.peek();
            if (c != null) {
                text = Thread.currentThread().getName() + " at time " + ((SimulationManager.currentTime) - 1) + ": " + clients + "\n";
                SimulationManager.write(text);

                c.setServiceTime(c.getServiceTime() - 1);

                waitingPeriod.getAndDecrement();

                if (c.getServiceTime() == 0) {
                    clients.remove(c);
                }
            } else {
                text = Thread.currentThread().getName() + " at time " + ((SimulationManager.currentTime) - 1) + ": " + " is closed" + "\n";
                SimulationManager.write(text);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public BlockingQueue<Task> getClients() {
        return clients;
    }

    public int getWaitingPeriod() {
        return waitingPeriod.intValue();
    }

    @Override
    public String toString() {
        return clients + " ";
    }
}
