import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;

import java.util.regex.Matcher;

public class BotRequestHandler implements com.sun.net.httpserver.HttpHandler {

    /**
     * Handles requests - currently
     * @param httpExchange ?? dont know wher this comes from
     * @throws IOException if something fails during output.
     */

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String requestParamValue = null;
        if("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        } else { // Post method used!!!

            //requestParamValue = handlePostRequest(httpExchange); --> Handle Post not recognized

        }

        handleResponse(httpExchange,requestParamValue);

    }

    /**
     * Transforms 'httpExchaange' into the requested URL String
     * @param httpExchange Input Object
     * @return requested page URL String
     */
    private String handleGetRequest(HttpExchange httpExchange) {

        return httpExchange
                .getRequestURI()
                .toString()
                .split(Regex.FIRST_QUESTION_MARK)[1];
    }

    /**
     *
     * @param httpExchange
     * @param requestParamValue URL of the requested action
     * @throws IOException
     */
    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {

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
        switch (urlAction) {
            case "add":
                htmlResponse = "<html>" +
                        "<body>" +
                        "<h1>" +
                        "Create new bot " +
                        urlActionParam +
                        "</h1>" +
                        "</body>" +
                        "</html>"
                // encode HTML content
                ;
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
