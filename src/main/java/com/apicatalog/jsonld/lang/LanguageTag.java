package com.apicatalog.jsonld.lang;

import java.util.stream.IntStream;

import com.apicatalog.rdf.lang.RdfAlphabet;

public final class LanguageTag {

    private LanguageTag() {
    }

    /**
     * LANGTAG  ::= [a-zA-Z]+ ('-' [a-zA-Z0-9]+)*
     * 
     * @see <a href="https://www.w3.org/TR/n-quads/#sec-grammar">N-Quads Grammar</a>
     * 
     * @param languageTag to check
     * @return <code>true</code> if the provided value is well-formed language tag
     * 
     */
    public static boolean isWellFormed(final String languageTag) {
        
        if (languageTag == null) {
            throw new IllegalArgumentException();
        }

        if (languageTag.isBlank()) {
            return false;
        }

        int[] chars = languageTag.trim().codePoints().toArray();

        if (RdfAlphabet.ASCII_ALPHA.negate().test(chars[0])) {
            return false;
        }
        
        if (chars.length == 1) {
            return true;
        }

        if (RdfAlphabet.ASCII_ALPHA_NUM.negate().test(chars[chars.length - 1])) {
            return false;
        }
        return IntStream.range(1, chars.length - 1).map(i -> chars[i]).allMatch(RdfAlphabet.ASCII_ALPHA_NUM.or(ch -> ch == '-'));
    }
}