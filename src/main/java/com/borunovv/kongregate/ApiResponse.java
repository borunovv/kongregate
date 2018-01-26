package com.borunovv.kongregate;

import java.io.IOException;

/**
 * @author borunovv
 */
class ApiResponse extends JsonReader {

    private static final String PARAM_SUCCESS = "success";
    private String originJson;

    @SuppressWarnings("unchecked")
    ApiResponse(String json) throws IOException {
        super(json);
        this.originJson = json;
    }

    /**
     * Check response status for success.
     *
     * @throws KongregateException if request status is not success (failed).
     */
    ApiResponse ensureSuccessStatus() throws KongregateException {
        // Check if status is success.
        if (!ensureBooleanParam(PARAM_SUCCESS)) {
            throw new KongregateException("Expected success response. Actual response:\n" + originJson);
        }
        return this;
    }

    @Override
    public String toString() {
        return originJson;
    }
}
