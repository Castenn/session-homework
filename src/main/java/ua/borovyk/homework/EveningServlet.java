package ua.borovyk.homework;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebServlet("/evening")
public class EveningServlet extends HttpServlet {

    private static final String DEFAULT_NAME = "Buddy";

    private static final String NAME_PARAMETER = "name";

    private static final String CUSTOM_SESSION = "CUSTOM_SESSION";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var writer = resp.getWriter();
        writer.printf("Good evening, %s", getName(req, resp));
    }

    private String getName(HttpServletRequest req, HttpServletResponse resp) {
        var parameterName = req.getParameter(NAME_PARAMETER);
        var customSessionCookie = getCustomSessionCookie(req);
        var optSessionId = customSessionCookie.map(Cookie::getValue);

        if (parameterName != null) {
            var sessionId = optSessionId.orElseGet(SessionStorage::generateSessionId);
            SessionStorage.putValue(sessionId, NAME_PARAMETER, parameterName);
            if (customSessionCookie.isEmpty()) {
                resp.addCookie(new Cookie(CUSTOM_SESSION, sessionId));
            }
            return parameterName;
        } else if (optSessionId.isPresent()) {
            var sessionName = SessionStorage.getValue(optSessionId.get(), NAME_PARAMETER);
            return sessionName.orElse(DEFAULT_NAME);
        }

        return DEFAULT_NAME;
    }

    private Optional<Cookie> getCustomSessionCookie(HttpServletRequest req) {
        var cookies = req.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> CUSTOM_SESSION.equals(cookie.getName()))
                .findAny();
    }

}
