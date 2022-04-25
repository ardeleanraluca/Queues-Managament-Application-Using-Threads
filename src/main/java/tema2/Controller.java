package tema2;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Controller {

    @FXML
    public TextArea rezultTextArea;
    @FXML
    private TextField numberOfQueues;

    @FXML
    private TextField numberOfClients;

    @FXML
    private TextField minArrivingTime;

    @FXML
    private TextField maxArrivingTime;

    @FXML
    private TextField minProcessingTime;

    @FXML
    private TextField maxProcessingTime;

    @FXML
    private TextField timeSimulation;


    @FXML
    public int getNumberOfQueues() {
        return Integer.parseInt(numberOfQueues.getText());
    }

    @FXML
    public int getNumberOfClients() {
        return Integer.parseInt(numberOfClients.getText());
    }

    @FXML
    public int getMinArrivingTime() {
        return Integer.parseInt(minArrivingTime.getText());
    }

    @FXML
    public int getMaxArrivingTime() {
        return Integer.parseInt(maxArrivingTime.getText());
    }

    @FXML
    public int getMinProccesingTime() {
        return Integer.parseInt(minProcessingTime.getText());
    }

    @FXML
    public int getMaxProcessingTime() {
        return Integer.parseInt(maxProcessingTime.getText());
    }

    @FXML
    public int getTimeSimulation() {
        return Integer.parseInt(timeSimulation.getText());
    }


    @FXML
    protected void Start() {
        SimulationManager gen = new SimulationManager(
                getNumberOfQueues(),
                getNumberOfClients(),
                getMinArrivingTime(),
                getMaxArrivingTime(),
                getMinProccesingTime(),
                getMaxProcessingTime(),
                getTimeSimulation()
        );

        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }

            @Override
            public void run() {
                final int max = getTimeSimulation() + 1; //time
                for (int i = 1; i <= max; i++) {
                    String result = "";
                    try {
                        result = Files.readString(SimulationManager.fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    rezultTextArea.appendText(SimulationManager.textt+Server.textt);
                    rezultTextArea.setText(result);
                    rezultTextArea.setScrollTop(Double.MAX_VALUE);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        };

        new Thread(task).start();

        Thread t = new Thread(gen);
        t.start();
    }


}


