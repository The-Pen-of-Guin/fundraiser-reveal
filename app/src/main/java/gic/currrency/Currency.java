package gic.currrency;

public class Currency {
    long amountCents;

    public Currency(long amountCents) {
        this.amountCents = amountCents;
    }

    public Currency(String amountString) {
        if (amountString == null || amountString.isEmpty()) {
            throw new IllegalArgumentException("The String must be in the USD format. Example: $123,456.78");
        }

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

    public void add(long delta) {
        if (amountCents + delta < 0) {
            throw new ArithmeticException("Currency cannot be negative!");
        }

        amountCents = Math.addExact(amountCents, delta);
    }

    public long getAmountCents() {
        return amountCents;
    }
}
