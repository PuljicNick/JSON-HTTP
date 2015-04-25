package http_request;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.util.concurrent.ExecutionException;
//Group developed in class - Not part of IA Code just for HTTP usage

public abstract class HttpResponseHandler implements EventHandler<WorkerStateEvent> {

    public abstract void handle(String response);

    @Override
    public void handle(WorkerStateEvent workerStateEvent) {
        HttpRequestTask task = (HttpRequestTask) workerStateEvent.getSource();

        try {
            String r = task.get();
            handle(r);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
