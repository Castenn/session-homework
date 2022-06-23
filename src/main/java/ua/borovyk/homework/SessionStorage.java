package ua.borovyk.homework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SessionStorage {

    private SessionStorage() {}

    private static final Map<String, Map<String, String>> STORAGE = new HashMap<>();

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public static void putValue(String sessionId, String parameter, String value) {
        if (sessionId == null) {
            sessionId = generateSessionId();
        }
        var session = STORAGE.get(sessionId);
        if (session == null) {
            session = new HashMap<>();
            STORAGE.put(sessionId, session);
        }
        session.put(parameter, value);
    }

    public static Optional<String> getValue(String sessionId, String nameParameter) {
        var session = STORAGE.get(sessionId);
        if (session == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(session.get(nameParameter));
    }

}
