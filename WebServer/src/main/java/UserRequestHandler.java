import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;

public class UserRequestHandler implements HttpHandler {

    /**
     * Handles requests - currently
     *
     * @param httpExchange ?? don't know where this comes from
     * @throws IOException if something fails during output.
     */

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String requestParamValue;

        // if a wrong type is used, then a 405 error should be sent.

        if ("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        } else { // Post method used!!!

            requestParamValue = handlePostRequest(httpExchange);

        }

        handleResponse(httpExchange, requestParamValue);

    }

    /**
     * Transforms 'httpExchaange' into the requested URL String
     *
     * @param httpExchange Input Object
     * @return requested page URL String
     */
    private String handleGetRequest(HttpExchange httpExchange) {

        return httpExchange
                .getRequestURI()
                .toString()
                .split(Regex.FIRST_QUESTION_MARK)[1];
    }

    private String handlePostRequest(HttpExchange httpExchange) {
        return httpExchange.getRequestHeaders().getFirst("POST"); //Only for testing it wont work!
    }


    /**
     * @param httpExchange
     * @param requestParamValue URL of the requested action
     * @throws IOException
     */

    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {

        Matcher matcher = Regex.getPattern(Regex.VALID_PARAM).matcher(requestParamValue);

        boolean matchFound = matcher.matches();

        String urlAction = "";
        String urlActionParam = "";
        if (matchFound) {
            urlAction = requestParamValue.split(Regex.EQUAL)[0];
            urlActionParam = requestParamValue.split(Regex.EQUAL)[1];
        }

        OutputStream outputStream = httpExchange.getResponseBody();

        String htmlResponse;
        // encode HTML content
        boolean success = false;

        switch (urlAction) {
            case "add":

                if (success) {
                    htmlResponse = String.format("{%s:%s, %s:%s, %s:%s}", JSONVars.TARGET, JSONVars.TARGET_USER, JSONVars.SUCCESS, JSONVars.TRUE, JSONVars.ACTION, JSONVars.USER_ACTION_ADD);
                } else {
                    htmlResponse = String.format("{%s:%s, %s:%s, %s:%s}", JSONVars.TARGET, JSONVars.TARGET_USER, JSONVars.SUCCESS, JSONVars.FALSE, JSONVars.ACTION, JSONVars.USER_ACTION_ADD);
                }
                break;

            case "delete":

                if (success) {
                    htmlResponse = String.format("{%s:%s, %s:%s, %s:%s}", JSONVars.TARGET, JSONVars.TARGET_USER, JSONVars.SUCCESS, JSONVars.TRUE, JSONVars.ACTION, JSONVars.USER_ACTION_DELETE);
                } else {
                    htmlResponse = String.format("{%s:%s, %s:%s, %s:%s}", JSONVars.TARGET, JSONVars.TARGET_USER, JSONVars.SUCCESS, JSONVars.FALSE, JSONVars.ACTION, JSONVars.USER_ACTION_DELETE);
                }

                break;


            case "changePassword":

                if (success) {
                    htmlResponse = String.format("{%s:%s, %s:%s, %s:%s}", JSONVars.TARGET, JSONVars.TARGET_USER, JSONVars.SUCCESS, JSONVars.TRUE, JSONVars.ACTION, JSONVars.USER_ACTION_CHANGE_PASSWORD);
                } else {
                    htmlResponse = String.format("{%s:%s, %s:%s, %s:%s}", JSONVars.TARGET, JSONVars.TARGET_USER, JSONVars.SUCCESS, JSONVars.FALSE, JSONVars.ACTION, JSONVars.USER_ACTION_CHANGE_PASSWORD);
                }

                break;


            case "login":

                // mit User und Passwort hash
                if (success) {
                    htmlResponse = String.format("{%s:%s, %s:%s, %s:%s}", JSONVars.TARGET, JSONVars.TARGET_USER, JSONVars.SUCCESS, JSONVars.TRUE, JSONVars.ACTION, JSONVars.USER_ACTION_LOG_IN);
                } else {
                    htmlResponse = String.format("{%s:%s, %s:%s, %s:%s}", JSONVars.TARGET, JSONVars.TARGET_USER, JSONVars.SUCCESS, JSONVars.FALSE, JSONVars.ACTION, JSONVars.USER_ACTION_LOG_IN);
                }

                break;


            case "logout":
                // mit User und Passwort hash
                if (success) {
                    htmlResponse = String.format("{%s:%s, %s:%s, %s:%s}", JSONVars.TARGET, JSONVars.TARGET_USER, JSONVars.SUCCESS, JSONVars.TRUE, JSONVars.ACTION, JSONVars.USER_ACTION_LOG_OUT);
                } else {
                    htmlResponse = String.format("{%s:%s, %s:%s, %s:%s}", JSONVars.TARGET, JSONVars.TARGET_USER, JSONVars.SUCCESS, JSONVars.FALSE, JSONVars.ACTION, JSONVars.USER_ACTION_LOG_OUT);
                }

                break;


            default:
                htmlResponse = "<html>" +
                        "<body>" +
                        "<h1>" +
                        "Unknown Action" +
                        "</h1>" +
                        "</body>" +
                        "</html>";
        }

        // this line is a must
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}