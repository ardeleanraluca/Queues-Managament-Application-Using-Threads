package tema2;


import java.util.List;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task c) {
        int shortestSize = servers.get(0).getClients().size();
        Server shortest = servers.get(0);
        for (Server s : servers) {
            int newShortestSize = s.getClients().size();
            if (newShortestSize < shortestSize) {
                shortestSize = newShortestSize;
                shortest = s;
            }
        }
        shortest.addClient(c);
    }
}
