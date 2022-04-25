package tema2;

import java.util.ArrayList;
import java.util.List;

enum SelectionPolicy {
    SHORTEST_QUEUE, SHORTEST_TIME
}

public class Scheduler {
    private List<Server> servers;
    private List<Thread> threads;
    private Strategy strategy;

    public Scheduler(int maxNrServers) {
        servers = new ArrayList<>();
        threads = new ArrayList<>();
        for (int i = 0; i < maxNrServers; i++) {
            Server server = new Server();
            servers.add(server);
            Thread thread = new Thread(server, "Queue_" + (i + 1));
            threads.add(thread);
            thread.start();

        }
        strategy = new ConcreteStrategyQueue();
        System.out.println("Scheduler done");
    }


    public void changeStrategy(SelectionPolicy selectionPolicy) {
        if (selectionPolicy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ConcreteStrategyQueue();
        if (selectionPolicy == SelectionPolicy.SHORTEST_TIME)
            strategy = new ConcreteStrategyTime();
    }

    private List<Server> chooseStrategy() {
        int shortestSize = servers.get(0).getClients().size();
        List<Server> shortests = new ArrayList<>();
        for (Server s : servers) {
            int newShortestSize = s.getClients().size();
            if (newShortestSize < shortestSize) {
                shortestSize = newShortestSize;
            }
        }

        for (Server s : servers) {
            int size = s.getClients().size();
            if (size == shortestSize) {
                shortests.add(s);
            }
        }
        return shortests;
    }

    public void dispatchTask(Task t) {
        List<Server> shortests = chooseStrategy();

        if (shortests.size() == 1) {
            changeStrategy(SelectionPolicy.SHORTEST_QUEUE);
            strategy.addTask(servers, t);
        } else {
            changeStrategy(SelectionPolicy.SHORTEST_TIME);
            strategy.addTask(shortests, t);
        }
    }

    public List<Server> getServers() {
        return servers;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    @Override
    public String toString() {
        return servers + " ";
    }
}
