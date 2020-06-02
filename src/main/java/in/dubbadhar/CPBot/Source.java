package in.dubbadhar.CPBot;

public enum Source {
    CodeChef, CodeForces, NO_PREFERENCE;

    static Source fromString(String name)
    {
        return switch (name)
                {
                    case "CodeForces" -> CodeForces;
                    case "CodeChef" -> CodeChef;
                    default -> null;
                };
    }
}
