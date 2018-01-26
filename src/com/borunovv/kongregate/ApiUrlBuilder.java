package com.borunovv.kongregate;

import com.borunovv.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Helper to build API URLs.
 *
 * @author borunovv
 */
class ApiUrlBuilder {
    private String urlWithoutURIParams;
    private Map<String, Object> params = new LinkedHashMap<>();

    private static final String PARAM_API_KEY = "api_key";

    /**
     * C-tor.
     *
     * @param apiKey              API key for project.
     *                            You can retrieve the API key by adding /api on to the end of the full URL for your game.
     *                            For example: http://www.kongregate.com/games/BenV/mygame/api
     * @param urlWithoutURIParams like 'http://aaa.bbb/path/to/resource/'
     */
    ApiUrlBuilder(String apiKey, String urlWithoutURIParams) {
        this.urlWithoutURIParams = urlWithoutURIParams;
        withParam(PARAM_API_KEY, apiKey);
    }

    /**
     * Append param to url as 'name=value' pair. Separated by '?' or '&'
     */
    ApiUrlBuilder withParam(String name, Object value) {
        this.params.put(name, value);
        return this;
    }

    /**
     * Build and return full URL with all params
     * like 'http://aaa.bbb/path/to/resource/?a=1&b=2'
     */
    String build() {
        StringBuilder sb = new StringBuilder(urlWithoutURIParams);
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String safeName = StringUtils.urlEncode(entry.getKey());
            String safeValue = StringUtils.urlEncode(entry.getValue() != null ?
                    entry.getValue().toString() :
                    "");
            sb.append(first ? '?' : '&');
            sb.append(safeName).append('=').append(safeValue);
            first = false;
        }

        return sb.toString();
    }
}
