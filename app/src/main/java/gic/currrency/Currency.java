package gic.currrency;

/**
 * The Currency class gives a mutable representation of the value that will be
 * displayed for the reveal.
 *
 * Internally, all money values are represented in US cents as long integers.
 * This avoids common issues from floating point representations.
 */
public class Currency {
    /**
     * The amount in cents.
     */
    long amountCents;

    /**
     * Create a currency using the amount in cents represented by a long integer.
     */
    public Currency(long amountCents) {
        this.amountCents = amountCents;
    }

    /**
     * Create a currency using a Currency string representation.
     */
    public Currency(String amountString) {
        // Reject null and empty strings
        if (amountString == null || amountString.isEmpty()) {
            throw new IllegalArgumentException("The String must be in the USD format. Example: $123,456.78");
        }

        // Parse the string to remove non numeric characters and check that it is in the
        // Currency string representation.
        int stringLength = amountString.length();
        var sb = new StringBuilder("");
        for (int i = 0; i < stringLength; i++) {
            var character = amountString.charAt(i);

            switch (i) {
                case 0:
                    if (character == '$') {
                        continue;
                    }
                    break;
                default:
                    if (Character.isDigit(character)) {
                        sb.append(character);
                        continue;
                    } else if ((stringLength - i + 1) > 4 && (stringLength - i + 1) % 4 == 0 && character == ',') {
                        continue;
                    } else if (i == stringLength - 3 && character == '.') {
                        continue;
                    }
            }
            throw new IllegalArgumentException(
                    "The String must be in the USD format. Example: $123,456.78");
        }

        this.amountCents = Long.parseLong(sb.toString());
    }

    /**
     * Returns the Currency in its String representation.
     */
    @Override
    public String toString() {
        var amount = Long.toString(amountCents);
        int amountLength = amount.length();

        var sb = new StringBuilder("$");
        for (int i = 0; i < amountLength; i++) {
            sb.append(amount.charAt(i));

            if (i == amountLength - 3) {
                sb.append('.');
            } else if (i < amountLength - 3 && (amountLength - i) % 3 == 0) {
                sb.append(',');
            }
        }

        return sb.toString();
    }

    /**
     * Modifies the Currency amount by adding a signed cent amount.
     */
    public void add(long deltaCents) {
        if (amountCents + deltaCents < 0) {
            throw new ArithmeticException("Currency cannot be negative!");
        }

        amountCents = Math.addExact(amountCents, deltaCents);
    }

    /**
     * Get the Currency amount in cents.
     */
    public long getAmountCents() {
        return amountCents;
    }
}
