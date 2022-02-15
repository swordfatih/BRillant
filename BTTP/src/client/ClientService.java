package client;

public abstract class ClientService implements Runnable  {
    private ClientBTTP bttp;

    public ClientService(ClientBTTP bttp) {
        this.bttp = bttp;
    }

    public ClientBTTP getBttp() {
        return bttp;
    }

    public void stop() {
        Thread.currentThread().interrupt();
    }

    public abstract void run();
}
