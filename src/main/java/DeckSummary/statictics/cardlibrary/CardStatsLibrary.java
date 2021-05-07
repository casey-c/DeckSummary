package DeckSummary.statictics.cardlibrary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;

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

    public static StatEstimate getEstimateForCard(AbstractCard card, String statKey) {
        // Entries can have the suffix "+" to represent a special case for an upgraded card.
        if (card.upgraded) {
            String upgradedID = card.cardID + "+";
            if (hasEstimateForCard(card.cardID + "+", statKey)) {
                return statsLibrary.get(upgradedID).get(statKey);
            }
        }
        return statsLibrary.get(card.cardID).get(statKey);
    }
}
