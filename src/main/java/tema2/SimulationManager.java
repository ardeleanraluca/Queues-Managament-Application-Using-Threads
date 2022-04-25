package tema2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.List;


public class SimulationManager implements Runnable {

    static int currentTime = 0;
    private int timeLimit ;
    private int minProcessingTime;
    private int maxProcessingTime;

    private int minArrivingTime;
    private int maxArrivingTime;


    private int numberOfServers;
    private int numberOfClients;

    public static Path fileName = Path.of("src/main/resources/results");


    private Scheduler scheduler;

    private List<Task> generatedClients;

    public SimulationManager(int numberOfServers, int numberOfClients, int minArrivingTime, int maxArrivingTime, int minProcessingTime,int maxProcessingTime, int timeLimit) {
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.minArrivingTime = minArrivingTime;
        this.maxArrivingTime = maxArrivingTime;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;
        this.timeLimit = timeLimit;

        scheduler = new Scheduler(numberOfServers);
        generatedClients = generateNRandomClients(numberOfClients);

        try {
            Files.deleteIfExists(fileName);
        } catch (IOException e) {
            System.out.println("nu s-a putut sterge fisierul");
        }

        try {
            Files.writeString(fileName, "", StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("nu s-a putut crea fisierul");
        }

        System.out.println(generatedClients);
    }

    public List<Task> generateNRandomClients(int N) {
        List<Task> list = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            Task c = new Task(i + 1, minArrivingTime, maxArrivingTime, minProcessingTime, maxProcessingTime);
            list.add(c);
        }
        list.sort(Comparator.comparingInt(Task::getArrivalTime));
        return list;
    }

    private float averageServiceTime() {
        float averageServiceTime = 0;
        for (Task c : generatedClients) {
            averageServiceTime += c.getServiceTime();
        }
        return averageServiceTime / numberOfClients;
    }


    public static void write(String text) {
        System.out.print(text);

        try {
            Files.writeString(fileName, text, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("nu s-a putut scrie in fisier");
        }
    }

    public void run() {
        int nrClients = 0;
        int peakTime = 0;
        int pk = 0;
        String text = "";


        float averageServiceTime = averageServiceTime();

        while (currentTime < timeLimit) {
            text = "\nTime " + currentTime + "\n";
            write(text);

            int i = 0;
            while (i < generatedClients.size()) {
                if (generatedClients.get(i).getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(generatedClients.get(i));
                    generatedClients.remove(i);
                    i = 0;
                } else
                    i++;
            }

            int nr = 0;
            int a = maxProcessingTime + 1;
            for (Server s : scheduler.getServers()) {
                nr += s.getClients().size();
            }
            if (nr > nrClients) {
                nrClients = nr;
                peakTime = currentTime;
                for (Server s : scheduler.getServers()) {
                    Task t = s.getClients().peek();
                    if (t != null) {
                        int st = t.getServiceTime();
                        if (st < a) {
                            a = st;
                        }
                    }
                }

                pk = a;
                if (pk > timeLimit - peakTime)
                    pk = timeLimit - peakTime;

            }
            if (nrClients == 0)
                pk = 1;

            if (generatedClients.size() == 0) {
                {
                    int ok = 0;
                    for (Server s : scheduler.getServers()) {
                        if (s.getWaitingPeriod() != 0)
                            ok = 1;
                    }
                    if (ok == 0) {
                        text = "FINISH! There are no more clients in the waiting queue or at the service queues\n" + "Average waiting time: " +
                                Server.averageWaitingPeriod / numberOfClients + "s\n" + "Average service time: " + averageServiceTime + "s\n";
                        if (peakTime == 0 && pk == 1)
                            text += "Peak time doesn't exist: Queues are empty\n";
                        else
                            text += "First peak time: [" + peakTime + ", " + (peakTime + pk - 1) + "]\n";

                        write(text);

                        for (i = 0; i < scheduler.getThreads().size(); i++)
                            scheduler.getThreads().get(i).stop();
                        return;
                    } else {
                        text = "There are no clients in the waiting queue\n";
                        write(text);
                    }
                }
            } else {
                text = "Waiting clients " + ": " + generatedClients + "\n";
                write(text);
            }

            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        text = "FINISH! Average waiting time: " + Server.averageWaitingPeriod / numberOfClients + "s\n" + "Average service time: " + averageServiceTime + "s\n";
        if (peakTime == 0 && pk == 1)
            text += "Peak time doesn't exist: Queues are empty\n";
        else
            text += "First peak time: [" + peakTime + ", " + (peakTime + pk - 1) + "]\n";
        write(text);

        for (int i = 0; i < scheduler.getThreads().size(); i++)
            scheduler.getThreads().get(i).stop();

    }


}
