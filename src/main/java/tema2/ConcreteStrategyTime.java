package tema2;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task c) {
        int shortestTime = servers.get(0).getWaitingPeriod();
        Server shortest = servers.get(0);
        for (Server s : servers) {
            int newShortestTime = s.getWaitingPeriod();
            if (newShortestTime < shortestTime) {
                shortestTime = newShortestTime;
                shortest = s;
            }
        }
        shortest.addClient(c);
    }
}
