package DeckSummary.statictics.cardlibrary;

import DeckSummary.statictics.ast.Expression;
import DeckSummary.statictics.ast.Operation;
import DeckSummary.statictics.ast.StatValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;

public class CardStatsLibrary {
    public static HashMap<String, HashMap<String, StatEstimate>> statsLibrary = new HashMap<>();
    public static Type typeToken = new TypeToken<HashMap<String, HashMap<String, StatEstimate>>>(){}.getType();

    private static final Logger logger = LogManager.getLogger(CardStatsLibrary.class.getName());

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(StatEstimate.class, new StatEstimate.Deserializer())
            .registerTypeAdapter(Expression.class, new Expression.Deserializer())
            .registerTypeAdapter(Operation.Operator.class, new Operation.Operator.Deserializer())
            .registerTypeAdapter(StatValue.class, new StatValue.Deserializer())
            .create();

    public static void load() {
        logger.info("Loading card stats.");

        loadCardStats("red_cards.json");
        loadCardStats("green_cards.json");
        loadCardStats("blue_cards.json");
        loadCardStats("colourless_cards.json");

        logger.info("Finished loading card stats.");
    }

    private static void loadCardStats(String fileName) {
        logger.info("Loading card stats from " + fileName + ".");

        try {
            statsLibrary.putAll(gson.fromJson(new InputStreamReader(
                    CardStatsLibrary.class.getResourceAsStream("/DeckSummary/" + fileName)), typeToken));
            logger.info("Successfully loaded card stats from " + fileName + ".");
        } catch (JsonParseException exception) {
            logger.error("Failed to load card stats from " + fileName + ". Skipping.");
            exception.printStackTrace();
        }
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
