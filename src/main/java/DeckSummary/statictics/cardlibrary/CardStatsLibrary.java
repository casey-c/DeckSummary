package DeckSummary.statictics.cardlibrary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;

public class CardStatsLibrary {
    public static HashMap<String, HashMap<String, StatEstimate>> statsLibrary = new HashMap<>();
    public static Type typeToken = new TypeToken<HashMap<String, HashMap<String, StatEstimate>>>(){}.getType();

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(StatValue.class, new StatValue.Deserializer())
            .registerTypeAdapter(StatEstimate.class, new StatEstimate.Deserializer())
            .create();

    public static void load() {
        statsLibrary.putAll(gson.fromJson(new InputStreamReader(
                CardStatsLibrary.class.getResourceAsStream("/DeckSummary/red_cards.json")), typeToken));
    }

    public static boolean hasEstimateForCard(String cardID, String statKey) {
        return statsLibrary.containsKey(cardID) && statsLibrary.get(cardID).containsKey(statKey);
    }

    public static StatEstimate getEstimateForCard(String cardID, String statKey) {
        return statsLibrary.get(cardID).get(statKey);
    }
}
