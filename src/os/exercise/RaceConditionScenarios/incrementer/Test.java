package os.exercise.RaceConditionScenarios.incrementer;

public class Test {
    //can be shared
    private static String staticField;

    //can be shared
    private String classField;

    public String example() {
        //never shared
        String localVariable = "something";

        return localVariable;
    }

    public String example(String maybeShared) {
        String shouldbeProtected = maybeShared;
        return shouldbeProtected;
    }
}
