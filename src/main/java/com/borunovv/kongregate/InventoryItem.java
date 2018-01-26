package com.borunovv.kongregate;

/**
 * Kongregate inventory item.
 * See API response format at https://docs.kongregate.com/docs/server-api-user-items
 *
 * @author borunovv
 */
public class InventoryItem {
    private long id;
    private String identifier;
    private String name;
    private String description;
    private Integer remainingUses;
    private String data;

    InventoryItem(JsonReader jsonReader) throws KongregateException {
        this.id = jsonReader.ensureLongParam("id");
        this.identifier = jsonReader.ensureStringParam("identifier");
        this.name = jsonReader.ensureStringParam("name");
        this.description = jsonReader.ensureStringParam("description");
        this.remainingUses = jsonReader.tryGetParam("remaining_uses") == null ?
                null :
                (int) jsonReader.ensureLongParam("remaining_uses");
        this.data = jsonReader.hasParam("data") ?
                (String) jsonReader.tryGetParam("data") :
                null;
    }

    public long getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Warning: return null for endless items! Use isEndless() to check it.
    public Integer getRemainingUses() {
        return remainingUses;
    }

    public String getData() {
        return data;
    }

    public boolean isEndless() {
        return getRemainingUses() == null;
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", remainingUses=" + remainingUses +
                ", data='" + data + '\'' +
                '}';
    }
}
