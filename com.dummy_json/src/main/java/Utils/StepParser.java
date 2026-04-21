package Utils;

public class StepParser {

    public static String getMethod(String step) {

        if (step == null) return "GET";

        if (step.contains("POST")) return "POST";
        if (step.contains("GET")) return "GET";
        if (step.contains("PUT")) return "PUT";
        if (step.contains("DELETE")) return "DELETE";

        return "GET";
    }

    public static String getEndpoint(String step) {

        if (step == null) return "/todos";

        if (step.contains("/add")) return "/todos/add";
        if (step.contains("/todos")) return "/todos";

        return "/todos";
    }
}