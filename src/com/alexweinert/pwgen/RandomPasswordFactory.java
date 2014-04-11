package com.alexweinert.pwgen;

public class RandomPasswordFactory extends PasswordFactory {

    /** The pool from which to pick characters during password creation */
    private final String characterPool;

    protected RandomPasswordFactory(IRandom randomGenerator, boolean mayIncludeAmbiguous, boolean mayIncludeVowels,
            boolean mustIncludeSymbols, boolean mustIncludeDigits, boolean mustIncludeUppercase) {
        super(randomGenerator, mayIncludeAmbiguous, mayIncludeVowels, mustIncludeSymbols, mustIncludeDigits,
                mustIncludeUppercase);
        this.characterPool = this.getCharacters();
    }

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
        PasswordFactory factory = (new Builder(new RandomGenerator())).mustIncludeUppercase().mustIncludeSymbols()
                .mightNotBePronouncable().create();
        for (int i = 0; i < 20; ++i) {
            System.out.println(factory.getPassword(8));
        }
    }
}
