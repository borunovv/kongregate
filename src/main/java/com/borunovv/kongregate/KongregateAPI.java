package com.borunovv.kongregate;

import java.util.List;


/**
 * Kongregate server API client.
 * API docs: https://docs.kongregate.com
 *
 * @author borunovv
 */
public class KongregateAPI {

    private static final String URL_AUTH = "https://api.kongregate.com/api/authenticate.json";
    private static final String URL_USER_ITEMS = "https://api.kongregate.com/api/user_items.json";

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_GAME_AUTH_TOKEN = "game_auth_token";
    private static final String PARAM_ITEMS = "items";


    /**
     * API key for game project.
     * You can retrieve the API key by adding /api on to the end of the full URL for your game.
     * For example: http://www.kongregate.com/games/BenV/mygame/api
     */
    private String apiKey;

    /**
     * C-tor.
     *
     * @param apiKey API key for game project.
     *               You can retrieve the API key by adding /api on to the end of the full URL for your game.
     *               For example: http://www.kongregate.com/games/BenV/mygame/api
     */
    public KongregateAPI(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Check authorization.
     * <p>
     * API docs: https://docs.kongregate.com/docs/server-api-authenticate
     * <p>
     * Here we are expecting response Json like this:
     * <pre>
     * {
     *     "success": true,
     *     "username": "Terminator",
     *     "user_id": 1480702
     * }
     * </pre>
     *
     * @return user name.
     * @throws KongregateException on any error.
     */
    public String checkAuthorization(long userId, String gameAuthToken) throws KongregateException {
        // Make request and parse response json into 'key-value' pairs.
        ApiResponse response = makeAPIRequest(URL_AUTH)
                .withParam(PARAM_USER_ID, userId)
                .withParam(PARAM_GAME_AUTH_TOKEN, gameAuthToken)
                .execute()
                .ensureSuccessStatus();

        // Check if userId matches with server's response.
        if (response.ensureLongParam(PARAM_USER_ID) != userId) {
            throw new KongregateException("User ID mismatch: expected id: "
                    + userId + ". Actual: " + response.ensureLongParam(PARAM_USER_ID));
        }

        return response.ensureStringParam("username");
    }

    /**
     * Check inap purchase.
     * API docs: https://docs.kongregate.com/docs/server-api-user-items
     * <p>
     * Here we are expecting response Json like this:
     * <pre>
     * {
     *     "success": true,
     *     "items": [
     *         {
     *             "id": 100,
     *             "identifier": "sharp-sword",
     *             "name": "Sharp Sword",
     *             "description": "A sharp sword",
     *             "remaining_uses": null,
     *             "data": null
     *         },
     *         {
     *             "id": 101,
     *             "identifier": "potion",
     *             "name": "Potion",
     *             "description": "A Healing Potion",
     *             "remaining_uses": 10,
     *             "data": null
     *         }
     *     ]
     * }
     * </pre>
     *
     * @return Inventory item matched the purchase.
     * @throws KongregateException on any error.
     */
    @SuppressWarnings("unchecked")
    public InventoryItem checkPurchase(long userId, String gameAuthToken, long itemInstanceId) throws KongregateException {
        // Ask server for inventory.
        ApiResponse response = makeAPIRequest(URL_USER_ITEMS)
                .withParam(PARAM_USER_ID, userId)
                .withParam(PARAM_GAME_AUTH_TOKEN, gameAuthToken)
                .execute()
                .ensureSuccessStatus();

        // Try extract items list.
        List<JsonReader> items = response.tryGetObjectsList(PARAM_ITEMS);
        if (items == null || items.isEmpty()) {
            throw new KongregateException("User inventory is empty.");
        }

        // Enumerate items and try find one with id == itemInstanceId
        for (JsonReader itemReader : items) {
            InventoryItem item = new InventoryItem(itemReader);
            if (item.getId() == itemInstanceId) {
                // We found item with right id.
                // Ok, let's check if item is endless.
                if (item.isEndless()) {
                    // Good, the item exists and is endless, so it means that item was purchased correctly.
                    // Just return from here to indicate success purchase.
                    return item;
                } else {
                    // Check remaining uses.
                    if (item.getRemainingUses() > 0) {
                        // We have some remaining uses. Good.
                        // Just return from here to indicate success purchase.
                        return item;
                    } else {
                        throw new KongregateException("Item has already completely used (remaining uses <= 0)");
                    }
                }
            }
        }

        // We come here if no item found with requested id.
        // So this means the item wasn't purchased.
        throw new KongregateException("Item not found in inventory. User ID: "
                + userId + ", itemInstanceId: " + itemInstanceId);
    }

    private ApiRequest makeAPIRequest(String url) {
        return new ApiRequest(apiKey, url);
    }
}
