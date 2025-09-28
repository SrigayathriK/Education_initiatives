import java.util.*;

// =====================
// BEHAVIOURAL PATTERNS
// =====================

// Observer Pattern - Weather Station
interface WeatherObserver {
    void update(float temperature, float humidity);
}

class PhoneDisplay implements WeatherObserver {
    private String id;
    public PhoneDisplay(String id) { this.id = id; }
    public void update(float temperature, float humidity) {
        System.out.println("PhoneDisplay " + id + " shows Temperature: " + temperature + "°C, Humidity: " + humidity + "%");
    }
}

class WeatherStation {
    private List<WeatherObserver> observers = new ArrayList<>();
    public void addObserver(WeatherObserver o) { observers.add(o); }
    public void removeObserver(WeatherObserver o) { observers.remove(o); }
    public void notifyObservers(float temp, float hum) {
        for (WeatherObserver o : observers) o.update(temp, hum);
    }
}

// Command Pattern - Remote Control
interface Command {
    void execute();
}

class LightOnCommand implements Command {
    public void execute() { System.out.println("Light is ON"); }
}

class LightOffCommand implements Command {
    public void execute() { System.out.println("Light is OFF"); }
}

class RemoteControl {
    private Command command;
    public void setCommand(Command command) { this.command = command; }
    public void pressButton() { command.execute(); }
}

// =====================
// CREATIONAL PATTERNS
// =====================

// Builder Pattern - Custom Computer
class Computer {
    private String CPU;
    private String GPU;
    private int RAM;
    private int storage;
    private Computer(Builder builder) {
        this.CPU = builder.CPU;
        this.GPU = builder.GPU;
        this.RAM = builder.RAM;
        this.storage = builder.storage;
    }
    public String toString() {
        return "Computer [CPU=" + CPU + ", GPU=" + GPU + ", RAM=" + RAM + "GB, Storage=" + storage + "GB]";
    }
    static class Builder {
        private String CPU = "i5";
        private String GPU = "Integrated";
        private int RAM = 8;
        private int storage = 256;
        public Builder setCPU(String CPU) { this.CPU = CPU; return this; }
        public Builder setGPU(String GPU) { this.GPU = GPU; return this; }
        public Builder setRAM(int RAM) { this.RAM = RAM; return this; }
        public Builder setStorage(int storage) { this.storage = storage; return this; }
        public Computer build() { return new Computer(this); }
    }
}

// Prototype Pattern - Document Cloning
class Document implements Cloneable {
    private String title;
    private String content;
    public Document(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void setContent(String content) { this.content = content; }
    public String toString() { return "Document: " + title + " | " + content; }
    public Document clone() {
        try { return (Document) super.clone(); }
        catch (CloneNotSupportedException e) { return null; }
    }
}

// =====================
// STRUCTURAL PATTERNS
// =====================

// Facade Pattern - Home Automation
class Lights { void turnOn() { System.out.println("Lights ON"); } void turnOff() { System.out.println("Lights OFF"); } }
class AC { void turnOn() { System.out.println("AC ON"); } void turnOff() { System.out.println("AC OFF"); } }
class TV { void turnOn() { System.out.println("TV ON"); } void turnOff() { System.out.println("TV OFF"); } }

class HomeFacade {
    private Lights lights = new Lights();
    private AC ac = new AC();
    private TV tv = new TV();
    public void morningMode() { lights.turnOn(); ac.turnOff(); tv.turnOff(); }
    public void nightMode() { lights.turnOff(); ac.turnOn(); tv.turnOn(); }
}

// Proxy Pattern - File Access
interface FileAccess {
    void display();
}

class RealFile implements FileAccess {
    private String filename;
    public RealFile(String filename) { this.filename = filename; }
    public void display() { System.out.println("Accessing file: " + filename); }
}

class ProxyFile implements FileAccess {
    private RealFile realFile;
    private String filename;
    private boolean hasAccess;
    public ProxyFile(String filename, boolean hasAccess) { this.filename = filename; this.hasAccess = hasAccess; }
    public void display() {
        if (hasAccess) {
            if (realFile == null) realFile = new RealFile(filename);
            realFile.display();
        } else System.out.println("Access denied to file: " + filename);
    }
}

// =====================
// MAIN DEMO
// =====================
public class DesignPatterns {
    public static void main(String[] args) {

        System.out.println("=== Behavioural Patterns ===");
        // Observer
        WeatherStation ws = new WeatherStation();
        ws.addObserver(new PhoneDisplay("A1"));
        ws.addObserver(new PhoneDisplay("B2"));
        ws.notifyObservers(30.5f, 70f);

        // Command
        RemoteControl remote = new RemoteControl();
        remote.setCommand(new LightOnCommand()); remote.pressButton();
        remote.setCommand(new LightOffCommand()); remote.pressButton();

        System.out.println("\n=== Creational Patterns ===");
        // Builder
        Computer customPC = new Computer.Builder().setCPU("i9").setGPU("RTX 4090").setRAM(32).setStorage(2000).build();
        System.out.println(customPC);

        // Prototype
        Document template = new Document("Template1", "Base Content");
        Document clonedDoc = template.clone();
        clonedDoc.setContent("Customized Content");
        System.out.println(template);
        System.out.println(clonedDoc);

        System.out.println("\n=== Structural Patterns ===");
        // Facade
        HomeFacade home = new HomeFacade();
        home.morningMode();
        home.nightMode();

        // Proxy
        FileAccess secureFile = new ProxyFile("Secret.txt", false);
        FileAccess openFile = new ProxyFile("Public.txt", true);
        secureFile.display();
        openFile.display();
    }
}
