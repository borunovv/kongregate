package com.borunovv.kongregate;

import com.borunovv.util.UrlReader;

/**
 * Helper to send GET HTTP(s) requests to Kongregate API server.
 *
 * @author borunovv
 */
class ApiRequest {
    private static final int HTTP_STATUS_OK = 200;
    private final ApiUrlBuilder urlBuilder;

    ApiRequest(String apiKey, String urlWithoutURIParams) {
        this.urlBuilder = new ApiUrlBuilder(apiKey, urlWithoutURIParams);
    }

    /**
     * Add URI param to request.
     *
     * @return self
     * @see ApiUrlBuilder#withParam(String, Object)
     */
    ApiRequest withParam(String name, Object value) {
        this.urlBuilder.withParam(name, value);
        return this;
    }

    /**
     * Send GET HTTP request to API server.
     *
     * @return Response body (json).
     * @throws KongregateException on any error
     */
    ApiResponse execute() throws KongregateException {
        String apiUrl = urlBuilder.build();
        try {
            UrlReader.Response response = UrlReader.get(apiUrl);
            if (response.getStatus() != HTTP_STATUS_OK) {
                throw new KongregateException(""
                        + "Unexpected status from API server: " + response.getStatus()
                        + "\nUrl: " + apiUrl
                        + "\nResponse: \n" + response.getBodyAsString());
            }

            return new ApiResponse(response.getBodyAsString());
        } catch (Exception e) {
            throw new KongregateException("Failed to get API url: " + apiUrl, e);
        }
    }
}
