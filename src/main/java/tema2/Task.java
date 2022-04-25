package tema2;

import java.util.*;

public class Task implements Cloneable {
    private int ID;
    private int arrivalTime = 0;
    private int serviceTime = 0;

    public Task(int ID, int arrMin, int arrMax, int servMin, int servMax) {
        Random random = new Random();
        this.ID = ID;
        System.out.println(servMin+" "+servMax);
        arrivalTime = random.nextInt(arrMin, arrMax);
        serviceTime = random.nextInt(servMin, servMax);
    }


    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
        return "(" + ID + ", " + arrivalTime + ", " + serviceTime + ") ";
    }

}
