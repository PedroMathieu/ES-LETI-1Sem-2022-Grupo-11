package pt.iscte.server.controllers;

import org.json.simple.JSONObject;
import pt.iscte.server.ServerService;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a controller. It's made of a handle method,
 * which handles the req and res. A process method, that processes
 * the request parameters and a method to build a response. All server
 * controllers must extend this class.
 *
 * @author Jose Soares
 */
public abstract class Controller {

    /**
     * Builds a response map to be handled by handle(Request req, Response res).
     * To decide what and how to respond.
     *
     * @param jsonToSend json data to be sent, if any
     * @param error error data to be sent, if any
     * @param contextJson true if we have to set context to json
     * @param hasError true if we have to send an error (disregards json data to be sent)
     * @return response map made of all the parameters and their value
     */
    protected Map<String, Object> buildResponseMap(JSONObject jsonToSend, String error, boolean contextJson, boolean hasError) {
        Map<String, Object> response = new HashMap<>();

        // No need to call server service if there was no error
        if (hasError) {
            response.put("gotError", hasError);
            response.put("error", ServerService.sendErrorToUser(error));
        } else {
            response.put("gotError", hasError);
            response.put("error", "");
        }

        response.put("contextJson", contextJson);
        response.put("dataToSend", jsonToSend);
        return response;
    }

    /**
     * Process parameter data and do the logic for each controller.
     *
     * @param params parameters from front-end
     * @return response map with the parameters to be handled by the handler
     */
    public abstract Map<String, Object> process(Map<String, String> params);

    /**
     * Handles a request, processes it using process() and sends a response.
     *
     * @param req Spark request containing all request data
     * @param res Spark response, used to respond to a request
     * @return http response
     */
    public abstract Object handle(Request req, Response res);
}
