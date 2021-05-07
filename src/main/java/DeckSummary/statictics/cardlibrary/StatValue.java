package DeckSummary.statictics.cardlibrary;

import com.google.gson.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.lang.reflect.Type;
import java.util.Arrays;

public class StatValue {
    int value = 0;
    ValueType type = ValueType.NONE;

    public StatValue() { }

    public StatValue(int value, ValueType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        StatValue statValue = (StatValue) other;
        return value == statValue.value && type == statValue.type;
    }

    private static int getEnergyMaster() {
        if (AbstractDungeon.player == null) return 3;
        else return AbstractDungeon.player.energy.energyMaster;
    }

    public int ofCard(AbstractCard card) {
        switch (type) {
            case CONSTANT:
                return value;
            case DAMAGE:
                return card.baseDamage;
            case DAMAGE_TIMES_MAGIC_NUMBER:
                return card.baseDamage * card.baseMagicNumber;
            case DAMAGE_PLUS_MAGIC_NUMBER:
                return card.baseDamage + card.baseMagicNumber;
            case MULTIPLE_OF_DAMAGE:
                return card.baseDamage * value;
            case BLOCK:
                return card.baseBlock;
            case MAGIC_NUMBER:
                return card.baseMagicNumber;
            case BLOCK_TIMES_MAGIC_NUMBER:
                return card.baseBlock * card.baseMagicNumber;
            case BLOCK_PLUS_MAGIC_NUMBER:
                return card.baseBlock + card.baseMagicNumber;
            case MULTIPLE_OF_BLOCK:
                return card.baseBlock * value;
            case MULTIPLE_OF_MAGIC_NUMBER:
                return card.baseMagicNumber * value;
            case ENERGY:
                return getEnergyMaster();
            case MULTIPLE_OF_ENERGY:
                return getEnergyMaster() * value;
            case ENERGY_TIMES_DAMAGE:
                return getEnergyMaster() * card.baseDamage;
            case ENERGY_TIMES_BLOCK:
                return getEnergyMaster() * card.baseBlock;
            case ENERGY_TIMES_MAGIC_NUMBER:
                return getEnergyMaster() * card.baseMagicNumber;
            default:
                return 0;
        }
    }

    public static class Deserializer implements JsonDeserializer<StatValue> {
        @Override
        // A StatValue entry can be serialized in a few different ways to make entering values easier.
        // First, it can be represented by a number directly, when read, it will be interpreted as a constant.
        // Second, it can be represented by an enum value, the enum value represents the method used to calculate the
        // value of the stat. For example, it can be the product of the card's damage number and magic number, or
        // block and magic number.
        // Third, in case where a value and an instruction is needed to calculate the stat of the card, it can be
        // directly represented by an object. For example, it can be a multiple of a card's magic number, where the
        // multiple has to be specified in the value field.
        public StatValue deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext _context)
                throws JsonParseException {
            // If the entry is a number, interpret it as a constant.
            if (jsonElement.isJsonPrimitive()) {
                if (jsonElement.getAsJsonPrimitive().isNumber()) {
                    return new StatValue(jsonElement.getAsInt(), ValueType.CONSTANT);
                }
                if (jsonElement.getAsJsonPrimitive().isString()) {
                    // Otherwise interpret it as an enum value of ValueType.
                    return new StatValue(0, Arrays.stream(ValueType.values())
                            .filter((value) -> value.name().equals(jsonElement.getAsString()))
                            .findFirst().orElse(ValueType.NONE));
                }
            }
            // If the entry is an object, interpret its fields.
            if (jsonElement.isJsonObject()) {
                JsonObject object = jsonElement.getAsJsonObject();
                return new StatValue(object.get("value").getAsInt(),
                        // Can be using the default deserializer of ValueType, but prefer the stream because it looks
                        // nicer.
                        Arrays.stream(ValueType.values())
                        .filter((value) -> value.name().equals(object.get("type").getAsString()))
                        .findFirst().orElse(ValueType.CONSTANT));
            }

            return new StatValue(0, ValueType.NONE);
        }
    }
}
