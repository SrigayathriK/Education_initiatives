import java.util.*;

// ========================
// BEHAVIOURAL DESIGN PATTERNS
// ========================

// Observer Pattern
interface Observer {
    void update(String message);
}

class User implements Observer {
    private String name;
    User(String name) { this.name = name; }
    public void update(String message) {
        System.out.println(name + " received notification: " + message);
    }
}

interface Subject {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers(String message);
}

class SocialMediaAccount implements Subject {
    private List<Observer> followers = new ArrayList<>();
    public void attach(Observer o) { followers.add(o); }
    public void detach(Observer o) { followers.remove(o); }
    public void notifyObservers(String message) {
        for (Observer o : followers) o.update(message);
    }
}

// Strategy Pattern
interface PaymentStrategy {
    void pay(int amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) { System.out.println("Paid " + amount + " using Credit Card"); }
}

class PaypalPayment implements PaymentStrategy {
    public void pay(int amount) { System.out.println("Paid " + amount + " using Paypal"); }
}

class ShoppingCart {
    private PaymentStrategy paymentStrategy;
    public void setPaymentStrategy(PaymentStrategy strategy) { this.paymentStrategy = strategy; }
    public void checkout(int amount) { paymentStrategy.pay(amount); }
}

// ========================
// CREATIONAL DESIGN PATTERNS
// ========================

// Singleton Pattern
class Logger {
    private static Logger instance;
    private Logger() {}
    public static Logger getInstance() {
        if (instance == null) instance = new Logger();
        return instance;
    }
    public void log(String message) { System.out.println("Log: " + message); }
}

// Factory Pattern
interface Vehicle { void start(); }

class Car implements Vehicle { public void start() { System.out.println("Car started"); } }
class Bike implements Vehicle { public void start() { System.out.println("Bike started"); } }

class VehicleFactory {
    public static Vehicle createVehicle(String type) {
        if (type.equalsIgnoreCase("Car")) return new Car();
        else if (type.equalsIgnoreCase("Bike")) return new Bike();
        else return null;
    }
}

// ========================
// STRUCTURAL DESIGN PATTERNS
// ========================

// Adapter Pattern
interface Payment {
    void pay(int amount);
}

class NewPaymentGateway {
    public void makePayment(int amount) { System.out.println("Paid " + amount + " via NewPaymentGateway"); }
}

class PaymentAdapter implements Payment {
    private NewPaymentGateway newPayment;
    public PaymentAdapter(NewPaymentGateway newPayment) { this.newPayment = newPayment; }
    public void pay(int amount) { newPayment.makePayment(amount); }
}

// Decorator Pattern
interface Coffee {
    String getDescription();
    double cost();
}

class SimpleCoffee implements Coffee {
    public String getDescription() { return "Simple Coffee"; }
    public double cost() { return 50; }
}

abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    CoffeeDecorator(Coffee coffee) { this.coffee = coffee; }
}

class MilkDecorator extends CoffeeDecorator {
    MilkDecorator(Coffee coffee) { super(coffee); }
    public String getDescription() { return coffee.getDescription() + ", Milk"; }
    public double cost() { return coffee.cost() + 20; }
}

class SugarDecorator extends CoffeeDecorator {
    SugarDecorator(Coffee coffee) { super(coffee); }
    public String getDescription() { return coffee.getDescription() + ", Sugar"; }
    public double cost() { return coffee.cost() + 10; }
}

// ========================
// MAIN DEMO
// ========================

public class DesignPatternsDemo {
    public static void main(String[] args) {

        System.out.println("=== Behavioural Patterns ===");
        // Observer
        SocialMediaAccount account = new SocialMediaAccount();
        User alice = new User("Alice");
        User bob = new User("Bob");
        account.attach(alice);
        account.attach(bob);
        account.notifyObservers("New post uploaded!");
        
        // Strategy
        ShoppingCart cart = new ShoppingCart();
        cart.setPaymentStrategy(new CreditCardPayment());
        cart.checkout(500);
        cart.setPaymentStrategy(new PaypalPayment());
        cart.checkout(1000);

        System.out.println("\n=== Creational Patterns ===");
        // Singleton
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        logger1.log("Application started");
        System.out.println("Logger instances are same: " + (logger1 == logger2));

        // Factory
        Vehicle car = VehicleFactory.createVehicle("Car");
        Vehicle bike = VehicleFactory.createVehicle("Bike");
        car.start();
        bike.start();

        System.out.println("\n=== Structural Patterns ===");
        // Adapter
        Payment payment = new PaymentAdapter(new NewPaymentGateway());
        payment.pay(750);

        // Decorator
        Coffee coffee = new SimpleCoffee();
        coffee = new MilkDecorator(coffee);
        coffee = new SugarDecorator(coffee);
        System.out.println(coffee.getDescription() + " costs " + coffee.cost());
    }
}
