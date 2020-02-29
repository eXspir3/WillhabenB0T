import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;

import java.util.regex.Matcher;

public class BotRequestHandler implements com.sun.net.httpserver.HttpHandler {

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

        boolean success = false;

        // encode HTML content
        switch (urlAction) {
            case "add":



                if (success) {
                    htmlResponse = "<html>" +
                            "<body>" +
                            "<h1>" +
                            "Created new bot " +
                            urlActionParam +
                            "</h1>" +
                            "</body>" +
                            "</html>"
                    // encode HTML content
                    ;
                } else {
                    htmlResponse = "<html>" +
                            "<body>" +
                            "<h1>" +
                            "Failed to create new bot " +
                            urlActionParam +
                            "</h1>" +
                            "</body>" +
                            "</html>"
                    // encode HTML content
                    ;
                }

                break;

            case "delete":



                if (success) {
                    htmlResponse = "<html>" +
                            "<body>" +
                            "<h1>" +
                            "Delete bot " +
                            urlActionParam +
                            "</h1>" +
                            "</body>" +
                            "</html>"
                    // encode HTML content
                    ;
                } else {
                    htmlResponse = "<html>" +
                            "<body>" +
                            "<h1>" +
                            "Failed to delete bot " +
                            urlActionParam +
                            "</h1>" +
                            "</body>" +
                            "</html>"
                    // encode HTML content
                    ;
                }

                break;


            case "edit":

                if (success) {
                    htmlResponse = "<html>" +
                            "<body>" +
                            "<h1>" +
                            "Edit new bot " +
                            urlActionParam +
                            "</h1>" +
                            "</body>" +
                            "</html>"
                    // encode HTML content
                    ;
                } else {
                    htmlResponse = "<html>" +
                            "<body>" +
                            "<h1>" +
                            "Failed to edit new bot " +
                            urlActionParam +
                            "</h1>" +
                            "</body>" +
                            "</html>"
                    // encode HTML content
                    ;
                }

                break;


            case "start":

                if (success) {
                    htmlResponse = "<html>" +
                            "<body>" +
                            "<h1>" +
                            "Start new bot " +
                            urlActionParam +
                            "</h1>" +
                            "</body>" +
                            "</html>"
                    // encode HTML content
                    ;
                } else {
                    htmlResponse = "<html>" +
                            "<body>" +
                            "<h1>" +
                            "Failed to start new bot " +
                            urlActionParam +
                            "</h1>" +
                            "</body>" +
                            "</html>"
                    // encode HTML content
                    ;
                }

                break;


            case "stop":

                if (success) {
                    htmlResponse = "<html>" +
                            "<body>" +
                            "<h1>" +
                            "Stop bot " +
                            urlActionParam +
                            "</h1>" +
                            "</body>" +
                            "</html>"
                    // encode HTML content
                    ;
                } else {
                    htmlResponse = "<html>" +
                            "<body>" +
                            "<h1>" +
                            "Failed to stop bot " +
                            urlActionParam +
                            "</h1>" +
                            "</body>" +
                            "</html>"
                    // encode HTML content
                    ;
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