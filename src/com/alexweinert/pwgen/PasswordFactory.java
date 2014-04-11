package com.alexweinert.pwgen;

public abstract class PasswordFactory {
    /** The random number generator used for picking random characters from a pool */
    protected IRandom randomGenerator;

    /** True if the generated password may include ambiguous characters */
    protected boolean mayIncludeAmbiguous;

    /** True if the generated password may include vowels */
    protected boolean mayIncludeVowels;

    /** True if the generated password must include symbols */
    protected boolean mustIncludeSymbols;
    /** True if the generated password must include digits */
    protected boolean mustIncludeDigits;
    /** True if the generated password must include uppercase characters */
    protected boolean mustIncludeUppercase;

    /** Pool from which to pick digits */
    protected final String pw_digits = "0123456789";
    /** Pool from which to pick uppercase characters */
    protected final String pw_uppers = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /** Pool from which to pick lowercase characters */
    protected final String pw_lowers = "abcdefghijklmnopqrstuvwxyz";
    /** Pool from which to pick symbols */
    protected final String pw_symbols = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    /** Pool from which to pick ambiguous characters */
    protected final String pw_ambiguous = "B8G6I1l0OQDS5Z2";
    /** Pool from which to pick vowels */
    protected final String pw_vowels = "01aeiouyAEIOUY";

    protected PasswordFactory(IRandom randomGenerator, boolean mayIncludeAmbiguous, boolean mayIncludeVowels,
            boolean mustIncludeSymbols, boolean mustIncludeDigits, boolean mustIncludeUppercase) {
        this.randomGenerator = randomGenerator;
        this.mayIncludeAmbiguous = mayIncludeAmbiguous;
        this.mayIncludeVowels = mayIncludeVowels;
        this.mustIncludeSymbols = mustIncludeSymbols;
        this.mustIncludeDigits = mustIncludeDigits;
        this.mustIncludeUppercase = mustIncludeUppercase;
    }

    public static class Builder {
        private IRandom randomGenerator;

        private boolean mayIncludeAmbiguous = true;
        private boolean mayIncludeVowels = true;

        private boolean mustIncludeSymbols = false;
        private boolean mustIncludeDigits = false;
        private boolean mustIncludeUppercase = false;

        private boolean mustBePronouncable = true;

        public Builder(IRandom randomGenerator) {
            this.randomGenerator = randomGenerator;
        }

        public PasswordFactory create() {
            PasswordFactory returnValue;
            if (this.mustBePronouncable) {
                returnValue = new PronouncablePasswordFactory(this.randomGenerator, this.mayIncludeAmbiguous,
                        this.mayIncludeVowels, this.mustIncludeSymbols, this.mustIncludeDigits,
                        this.mustIncludeUppercase);
            } else {
                returnValue = new RandomPasswordFactory(this.randomGenerator, this.mayIncludeAmbiguous,
                        this.mayIncludeVowels, this.mustIncludeSymbols, this.mustIncludeDigits,
                        this.mustIncludeUppercase);
            }

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

        public Builder mustBePronouncable() {
            this.mustBePronouncable = true;
            return this;
        }

        public Builder mightNotBePronouncable() {
            this.mustBePronouncable = false;
            return this;
        }
    }

    public abstract String getPassword(int length);
}
