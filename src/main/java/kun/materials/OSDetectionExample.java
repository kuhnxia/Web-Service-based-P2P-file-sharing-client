package kun.materials;

public class OSDetectionExample {
    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println(os);

        if (os.contains("win")) {
            System.out.println("Windows");
        } else if (os.contains("mac")) {
            System.out.println("MacOS");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("uni")) {
            System.out.println("Unix/Linux");
        } else {
            System.out.println("Unknown OS");
        }
    }
}

