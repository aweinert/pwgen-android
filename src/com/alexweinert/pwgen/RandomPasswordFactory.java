package com.alexweinert.pwgen;

public class RandomPasswordFactory extends PasswordFactory {

    public static class Builder {
        private IRandom randomGenerator;

        private boolean mayIncludeAmbiguous = true;
        private boolean mayIncludeVowels = true;

        private boolean mustIncludeSymbols = false;
        private boolean mustIncludeDigits = false;
        private boolean mustIncludeUppercase = false;

        public Builder(IRandom randomGenerator) {
            this.randomGenerator = randomGenerator;
        }

        public RandomPasswordFactory create() {
            RandomPasswordFactory returnValue = new RandomPasswordFactory(this.randomGenerator,
                    this.mayIncludeAmbiguous, this.mayIncludeVowels, this.mustIncludeSymbols, this.mustIncludeDigits,
                    this.mustIncludeUppercase);

            return returnValue;
        }

        public Builder mayIncludeAmbiguous() {
            this.mayIncludeAmbiguous = true;
            return this;
        }

        public Builder mustNotIncludeAmbiguous() {
            this.mayIncludeAmbiguous = false;
            return this;
        }

        public Builder mayIncludeVowels() {
            this.mayIncludeVowels = true;
            return this;
        }

        public Builder mustNotIncludeVowels() {
            this.mayIncludeVowels = false;
            return this;
        }

        public Builder mustIncludeSymbols() {
            this.mustIncludeSymbols = true;
            return this;
        }

        public Builder mightNotIncludeSymbols() {
            this.mustIncludeSymbols = false;
            return this;
        }

        public Builder mustIncludeDigits() {
            this.mustIncludeDigits = true;
            return this;
        }

        public Builder mightNotIncludeDigits() {
            this.mustIncludeDigits = false;
            return this;
        }

        public Builder mustIncludeUppercase() {
            this.mustIncludeUppercase = true;
            return this;
        }

        public Builder mightNotIncludeUppercase() {
            this.mustIncludeUppercase = false;
            return this;
        }
    }

    /** Hide default constructor, this class can only be constructed via the builder */
    private RandomPasswordFactory(IRandom randomGenerator, boolean mayIncludeAmbiguous, boolean mayIncludeVowels,
            boolean mustIncludeSymbols, boolean mustIncludeDigits, boolean mustIncludeUppercase) {
        this.randomGenerator = randomGenerator;
        this.mayIncludeAmbiguous = mayIncludeAmbiguous;
        this.mayIncludeVowels = mayIncludeVowels;
        this.mustIncludeSymbols = mustIncludeSymbols;
        this.mustIncludeDigits = mustIncludeDigits;
        this.mustIncludeUppercase = mustIncludeUppercase;

        this.characterPool = this.getCharacters();
    }

    /** The random number generator used for picking random characters from a pool */
    IRandom randomGenerator;

    /** True if the generated password may include ambiguous characters */
    private boolean mayIncludeAmbiguous;

    /** True if the generated password may include vowels */
    private boolean mayIncludeVowels;

    /** True if the generated password must include symbols */
    private boolean mustIncludeSymbols;
    /** True if the generated password must include digits */
    private boolean mustIncludeDigits;
    /** True if the generated password must include uppercase characters */
    private boolean mustIncludeUppercase;

    /** Pool from which to pick digits */
    private final String pw_digits = "0123456789";
    /** Pool from which to pick uppercase characters */
    private final String pw_uppers = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /** Pool from which to pick lowercase characters */
    private final String pw_lowers = "abcdefghijklmnopqrstuvwxyz";
    /** Pool from which to pick symbols */
    private final String pw_symbols = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    /** Pool from which to pick ambiguous characters */
    private final String pw_ambiguous = "B8G6I1l0OQDS5Z2";
    /** Pool from which to pick vowels */
    private final String pw_vowels = "01aeiouyAEIOUY";

    /** The pool from which to pick characters during password creation */
    private final String characterPool;

    @Override
    public String getPassword(int length) {
        String password = null;
        do {
            StringBuilder passwordBuilder = new StringBuilder();
            while (passwordBuilder.length() < length) {
                char newCharacter = this.getAdmissableChar();
                passwordBuilder.append(newCharacter);
            }
            password = passwordBuilder.toString();
        } while (!this.isAdmissablePassword(password));

        return password;
    }

    private String getCharacters() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pw_lowers);
        if (this.mustIncludeDigits) {
            stringBuilder.append(pw_digits);
        }
        if (this.mustIncludeUppercase) {
            stringBuilder.append(pw_uppers);
        }
        if (this.mustIncludeSymbols) {
            stringBuilder.append(pw_symbols);
        }

        return stringBuilder.toString();
    }

    private char getRandomCharacterFromPool() {
        int max = this.characterPool.length();
        int position = this.randomGenerator.getRandomInt(max);
        return characterPool.charAt(position);
    }

    private char getAdmissableChar() {
        char returnValue;
        do {
            returnValue = this.getRandomCharacterFromPool();
        } while (!this.isAdmissableChar(returnValue));
        return returnValue;
    }

    private boolean isAdmissableChar(char character) {
        CharSequence currentCharSequence = String.valueOf(character);
        if (!this.mayIncludeAmbiguous && this.pw_ambiguous.contains(currentCharSequence)) {
            return false;
        }
        if (!this.mayIncludeVowels && this.pw_vowels.contains(currentCharSequence)) {
            return false;
        }
        return true;
    }

    private boolean isAdmissablePassword(String password) {
        boolean includesUppercase = false, includesDigits = false, includesSymbols = false;
        for (char character : password.toCharArray()) {
            CharSequence currentCharSequence = String.valueOf(character);
            if (this.pw_uppers.contains(currentCharSequence)) {
                includesUppercase = true;
            }
            if (this.pw_digits.contains(currentCharSequence)) {
                includesDigits = true;
            }
            if (this.pw_symbols.contains(currentCharSequence)) {
                includesSymbols = true;
            }
        }

        if (this.mustIncludeUppercase && !includesUppercase) {
            return false;
        }
        if (this.mustIncludeDigits && !includesDigits) {
            return false;
        }
        if (this.mustIncludeSymbols && !includesSymbols) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        RandomPasswordFactory factory = (new Builder(new RandomGenerator())).mustIncludeUppercase()
                .mustIncludeSymbols().create();
        for (int i = 0; i < 20; ++i) {
            System.out.println(factory.getPassword(8));
        }
    }
}
