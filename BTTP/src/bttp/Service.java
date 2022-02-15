package bttp;

public abstract class Service  {
    private ServiceBTTP bttp;

    public Service(ServiceBTTP bttp) {
        this.bttp = bttp;
    }

    public ServiceBTTP getBttp() {
        return bttp;
    }

    public abstract String toString();

    public abstract boolean run();
}
