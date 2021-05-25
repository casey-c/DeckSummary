package DeckSummary.statistics;

public class StatOutput {
    public String name;
    public int conservativeEstimate;
    public int optimisticEstimate;

    public StatOutput(String name, int conservativeEstimate, int optimisticEstimate) {
        this.name = name;
        this.conservativeEstimate = conservativeEstimate;
        this.optimisticEstimate = optimisticEstimate;
    }

    public String getValueString() {
        if (conservativeEstimate == optimisticEstimate) {
            return Integer.toString(conservativeEstimate);
        } else {
            return conservativeEstimate + "-" + optimisticEstimate;
        }
    }
}
