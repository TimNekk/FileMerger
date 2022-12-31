package timnekk;

public final class Main {
    public static void main(String[] args) {
        try (Application application = new Application(System.in, System.out)) {
            application.run();
        }
    }
}
