package os.exercise.consumerProducer.HelpingClasses;

public class Product {
    String name;
    private static int count=0;
    public Product() {
        name = "Product-" + count++;
    }

    @Override
    public String toString() {
        return name;
    }
}
