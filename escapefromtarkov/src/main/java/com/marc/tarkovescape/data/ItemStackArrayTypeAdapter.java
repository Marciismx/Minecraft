package com.marc.tarkovescape.data;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Type;

public class ItemStackArrayTypeAdapter implements JsonSerializer<ItemStack[]>, JsonDeserializer<ItemStack[]> {
    @Override
    public JsonElement serialize(ItemStack[] src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        for (ItemStack itemStack : src) {
            jsonArray.add(itemStack == null ? JsonNull.INSTANCE : serializeItemStack(itemStack, context));
        }
        return jsonArray;
    }

    @Override
    public ItemStack[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        ItemStack[] itemStacks = new ItemStack[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            itemStacks[i] = jsonArray.get(i).isJsonNull() ? null : deserializeItemStack(jsonArray.get(i), context);
        }
        return itemStacks;
    }

    private JsonElement serializeItemStack(ItemStack itemStack, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", itemStack.getType().name());
        jsonObject.addProperty("amount", itemStack.getAmount());
        jsonObject.add("meta", context.serialize(itemStack.getItemMeta()));
        return jsonObject;
    }

    private ItemStack deserializeItemStack(JsonElement jsonElement, JsonDeserializationContext context) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ItemStack itemStack = new ItemStack(Material.valueOf(jsonObject.get("type").getAsString()), jsonObject.get("amount").getAsInt());
        ItemMeta itemMeta = context.deserialize(jsonObject.get("meta"), ItemMeta.class);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
