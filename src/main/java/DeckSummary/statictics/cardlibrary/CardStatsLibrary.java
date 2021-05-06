package DeckSummary.statictics.cardlibrary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;

public class CardStatsLibrary {
    public static HashMap<String, HashMap<String, StatValue>> statsLibrary = new HashMap<>();
    public static Type typeToken = new TypeToken<HashMap<String, HashMap<String, StatValue>>>(){}.getType();

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(StatValue.class, new StatValue.Deserializer()).create();

    public static void load() {
        statsLibrary.putAll(gson.fromJson(new InputStreamReader(
                CardStatsLibrary.class.getResourceAsStream("/DeckSummary/red_cards.json")), typeToken));
    }

    public static boolean hasStatForCard(String cardID, String statKey) {
        return statsLibrary.containsKey(cardID) && statsLibrary.get(cardID).containsKey(statKey);
    }

    public static StatValue getStatForCard(String cardID, String statKey) {
        return statsLibrary.get(cardID).get(statKey);
    }
}
