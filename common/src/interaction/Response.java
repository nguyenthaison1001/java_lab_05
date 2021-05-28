package interaction;

import data.LabWork;

import java.io.Serializable;
import java.util.LinkedList;

public class Response implements Serializable {
    LinkedList<LabWork> labCollection;
    private final ResponseCode responseCode;
    private final String responseBody;
    private final String[] responseBodyArgs;

    public Response(ResponseCode responseCode, String responseBody, String[] responseBodyArgs,
                    LinkedList<LabWork> labCollection) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.labCollection = labCollection;
        this.responseBodyArgs = responseBodyArgs;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String[] getResponseBodyArgs() {
        return responseBodyArgs;
    }

    /**
     * @return LabWork collection last save.
     */
    public LinkedList<LabWork> getLabCollection() {
        return labCollection;
    }



    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
