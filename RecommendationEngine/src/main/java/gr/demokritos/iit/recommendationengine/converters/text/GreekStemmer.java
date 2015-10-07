/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gr.demokritos.iit.recommendationengine.converters.text;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Built on the greek stemmer implementation by Tasos Stathopoulos (Skroutz),
 * which can be found at:
 * https://github.com/skroutz/elasticsearch-skroutz-greekstemmer/blob/master/src/main/java/org/elasticsearch/index/analysis/SkroutzGreekStemmer.java
 * Below I provide the original description: ============ A stemmer for Greek
 * words, according to: <i>Development of a Stemmer for the Greek Language.</i>
 * Georgios Ntais
 * <p>
 * NOTE: Input is expected to be casefolded for Greek (including folding of
 * final sigma to sigma), and with diacritics removed. This can be achieved with
 * either {@link GreekLowerCaseFilter} or ICUFoldingFilter.
 *
 * This stemmer is based on the stemmer of the @lucene.experimental with some
 * additions.
 * <p>
 * According to: <i>Development of a Stemmer for the Greek Language.</i>, the
 * original stemmer removed 158 suffixes. Eight suffixes were not handled at all
 * by this stemmer. However, four of those eight suffixes belong to the same
 * category with one of the suffixes that is actually handle by the stemmer.
 * This suffix is "ια" which is mishandled and may be the plural type of the
 * suffix "ιο".
 * <p>
 * Furthermore, the suffix "εασ" is not stemmed correctly and is not even
 * included in the original 166 suffixes that should be removed.
 * <p>
 * This custom Greek stemmer adds to the current rule set some more cases about
 * the following suffixes:
 * <p>
 * <b>"ιο", "ιασ", "ιεσ", "ιοσ", "ιουσ", "ιοι", "εασ", "εα"</b>
 * <p>
 * Following the same strategy with the Greek stemmer of lucene, some exceptions
 * about these suffixes are added. ============
 *
 */
public class GreekStemmer implements IStemmer {

    private final Set<String> stopwords;

    public GreekStemmer(final Set<String> stopwords) {
        if (stopwords == null) {
            this.stopwords = new HashSet<String>();
        } else {
            this.stopwords = stopwords;
        }
    }

    @Override
    public String stem(String sToStem) {
        // Init length to original string length
        int len = sToStem.length();

        StringBuilder s = normalize(sToStem);

        // Too short or a stopword
        if (len < 3 || contains(stopwords, s, 0, len)) {
            return s.toString();
        }

        final int origLen = len;
        // "short rules": if it hits one of these, it skips the "long list"
        len = rule0(s, len);
        len = rule1(s, len);
        len = rule2(s, len);
        len = rule3(s, len);
        len = rule4(s, len);
        len = rule5(s, len);
        len = rule6(s, len);
        len = rule7(s, len);
        len = rule8(s, len);
        len = rule9(s, len);
        len = rule10(s, len);
        len = rule11(s, len);
        len = rule12(s, len);
        len = rule13(s, len);
        len = rule14(s, len);
        len = rule15(s, len);
        len = rule16(s, len);
        len = rule17(s, len);
        len = rule18(s, len);
        len = rule19(s, len);
        len = rule20(s, len);
        // "long list"
        if (len == origLen) {
            len = rule21(s, len);
        }

        return sToStem.substring(0, rule22(s, len));
    }

    public StringBuilder normalize(String sToNormalize) {
        String s = sToNormalize;
        s = s.replaceAll("[άΆ]", "α");
        s = s.replaceAll("[έΈ]", "ε");
        s = s.replaceAll("[ήΉ]", "η");
        s = s.replaceAll("[ίΊϊΪΐ]", "ι");
        s = s.replaceAll("[όΌ]", "ο");
        s = s.replaceAll("[ύΎϋΫΰ]", "υ");
        s = s.replaceAll("[ώΏ]", "ω");
        s = s.toLowerCase().replaceAll("[ς]", "σ");

        return new StringBuilder(s);
    }

    private int rule0(StringBuilder s, int len) {
        if (len > 9 && (endsWith(s, len, "καθεστωτοσ")
                || endsWith(s, len, "καθεστωτων"))) {
            return len - 4;
        }

        if (len > 8 && (endsWith(s, len, "γεγονοτοσ")
                || endsWith(s, len, "γεγονοτων"))) {
            return len - 4;
        }

        if (len > 8 && endsWith(s, len, "καθεστωτα")) {
            return len - 3;
        }

        if (len > 7 && (endsWith(s, len, "τατογιου")
                || endsWith(s, len, "τατογιων"))) {
            return len - 4;
        }

        if (len > 7 && endsWith(s, len, "γεγονοτα")) {
            return len - 3;
        }

        if (len > 7 && endsWith(s, len, "καθεστωσ")) {
            return len - 2;
        }

        if (len > 6 && (endsWith(s, len, "σκαγιου"))
                || endsWith(s, len, "σκαγιων")
                || endsWith(s, len, "ολογιου")
                || endsWith(s, len, "ολογιων")
                || endsWith(s, len, "κρεατοσ")
                || endsWith(s, len, "κρεατων")
                || endsWith(s, len, "περατοσ")
                || endsWith(s, len, "περατων")
                || endsWith(s, len, "τερατοσ")
                || endsWith(s, len, "τερατων")) {
            return len - 4;
        }

        if (len > 6 && endsWith(s, len, "τατογια")) {
            return len - 3;
        }

        if (len > 6 && endsWith(s, len, "γεγονοσ")) {
            return len - 2;
        }

        if (len > 5 && (endsWith(s, len, "φαγιου")
                || endsWith(s, len, "φαγιων")
                || endsWith(s, len, "σογιου")
                || endsWith(s, len, "σογιων"))) {
            return len - 4;
        }

        if (len > 5 && (endsWith(s, len, "σκαγια")
                || endsWith(s, len, "ολογια")
                || endsWith(s, len, "κρεατα")
                || endsWith(s, len, "περατα")
                || endsWith(s, len, "τερατα"))) {
            return len - 3;
        }

        if (len > 4 && (endsWith(s, len, "φαγια")
                || endsWith(s, len, "σογια")
                || endsWith(s, len, "φωτοσ")
                || endsWith(s, len, "φωτων"))) {
            return len - 3;
        }

        if (len > 4 && (endsWith(s, len, "κρεασ")
                || endsWith(s, len, "περασ")
                || endsWith(s, len, "τερασ"))) {
            return len - 2;
        }

        if (len > 3 && endsWith(s, len, "φωτα")) {
            return len - 2;
        }

        if (len > 2 && endsWith(s, len, "φωσ")) {
            return len - 1;
        }

        return len;
    }

    private int rule1(StringBuilder s, int len) {
        if (len > 4 && (endsWith(s, len, "αδεσ") || endsWith(s, len, "αδων"))) {
            len -= 4;
            if (!(endsWith(s, len, "οκ")
                    || endsWith(s, len, "μαμ")
                    || endsWith(s, len, "μαν")
                    || endsWith(s, len, "μπαμπ")
                    || endsWith(s, len, "πατερ")
                    || endsWith(s, len, "γιαγι")
                    || endsWith(s, len, "νταντ")
                    || endsWith(s, len, "κυρ")
                    || endsWith(s, len, "θει")
                    || endsWith(s, len, "πεθερ")
                    || endsWith(s, len, "παρ")
                    || endsWith(s, len, "ψαρ")
                    || endsWith(s, len, "τζουρ")
                    || endsWith(s, len, "ταμπουρ")
                    || endsWith(s, len, "καπλαμ"))) {
                len += 2; // add back -αδ
            }
        }
        return len;
    }

    private int rule2(StringBuilder s, int len) {
        if (len > 4 && (endsWith(s, len, "εδεσ") || endsWith(s, len, "εδων"))) {
            len -= 4;
            if (endsWith(s, len, "οπ")
                    || endsWith(s, len, "ιπ")
                    || endsWith(s, len, "εμπ")
                    || endsWith(s, len, "υπ")
                    || endsWith(s, len, "γηπ")
                    || endsWith(s, len, "δαπ")
                    || endsWith(s, len, "κρασπ")
                    || endsWith(s, len, "μιλ")) {
                len += 2; // add back -εδ
            }
        }
        return len;
    }

    private int rule3(StringBuilder s, int len) {
        if (len > 5 && (endsWith(s, len, "ουδεσ") || endsWith(s, len, "ουδων"))) {
            len -= 5;
            if (endsWith(s, len, "αρκ")
                    || endsWith(s, len, "καλιακ")
                    || endsWith(s, len, "πεταλ")
                    || endsWith(s, len, "λιχ")
                    || endsWith(s, len, "πλεξ")
                    || endsWith(s, len, "σκ")
                    || endsWith(s, len, "σ")
                    || endsWith(s, len, "φλ")
                    || endsWith(s, len, "φρ")
                    || endsWith(s, len, "βελ")
                    || endsWith(s, len, "λουλ")
                    || endsWith(s, len, "χν")
                    || endsWith(s, len, "σπ")
                    || endsWith(s, len, "τραγ")
                    || endsWith(s, len, "φε")) {
                len += 3; // add back -ουδ
            }
        }
        return len;
    }

    /**
     * Exceptions added to the Skroutz Greek stemmer about suffixes "εασ", "εα",
     * in order to improve the quality of the query.
     * <p>
     * Exception examples:
     * <p>
     * <i>παρεα - παρεασ - παρεεσ, στερεα - στερεασ - στερεεσ</i>
     */
    private static final Set<String> exc4 = new HashSet<String>(
            Arrays.asList("θ", "δ", "ελ", "γαλ", "ν", "π", "ιδ", "παρ", "στερ",
                    "ορφ", "ανδρ", "αντρ"));

    private int rule4(StringBuilder s, int len) {
        boolean removed = false;

        if (len > 3 && (endsWith(s, len, "εωσ")
                || endsWith(s, len, "εων")
                || endsWith(s, len, "εασ"))) {
            len -= 3;
            removed = true;
        } else if (len > 2 && endsWith(s, len, "εα")) {
            len -= 2;
            removed = true;
        }

        if (removed && contains(exc4, s, 0, len)) {
            len += 1; // add back the "ε"
        }
        return len;
    }

    /**
     * Exceptions added to the Skroutz Greek stemmer about rule 5, in order to
     * improve the quality of the query.
     * <p>
     * Exception examples:
     * <p>
     * <i>ηλιος - ηλος, αγριος - αγρος, χωρα - χωριο, αγιος - αγων, φωτο -
     * φωτια, νοτα - νοτια, μπριος - μπρος, τηλιο - τηλος, δημος - δημιος,
     * οπαλιο - οπαλ, πατριος - πατρα, ποντος - ποντιος, σκορπω - σκορπιος,
     * σπανιος - σπανος, τιμή - τιμιος</i>
     */
    private static final Set<String> exc5 = new HashSet<String>(
            Arrays.asList("αγ", "αγγελ", "αγρ", "αερ", "αθλ", "ακουσ", "αξ", "ασ",
                    "β", "βιβλ", "βυτ", "γ", "γιαγ", "γων", "δ", "δαν", "δηλ", "δημ",
                    "δοκιμ", "ελ", "ζαχαρ", "ηλ", "ηπ", "ιδ", "ισκ", "ιστ", "ιον", "ιων",
                    "κιμωλ", "κολον", "κορ", "κτηρ", "κυρ", "λαγ", "λογ", "μαγ", "μπαν",
                    "μπρ", "ναυτ", "νοτ", "οπαλ", "οξ", "ορ", "οσ", "παναγ", "πατρ",
                    "πηλ", "πην", "πλαισ", "ποντ", "ραδ", "ροδ", "σκ", "σκορπ", "σουν",
                    "σπαν", "σταδ", "συρ", "τηλ", "τιμ", "τοκ", "τοπ", "τροχ", "χωρ",
                    "φιλ", "φωτ", "χ", "χιλ", "χρωμ")
    );

    private int rule5(StringBuilder s, int len) {
        boolean removed = false;
        if (len > 2 && (endsWith(s, len, "ιο")
                || endsWith(s, len, "ια"))) {
            len -= 2;
            removed = true;
        } else if (len > 3 && (endsWith(s, len, "ιασ")
                || endsWith(s, len, "ιεσ")
                || endsWith(s, len, "ιοσ")
                || endsWith(s, len, "ιου")
                || endsWith(s, len, "ιοι")
                || endsWith(s, len, "ιον")
                || endsWith(s, len, "ιων"))) {
            len -= 3;
            removed = true;
        } else if (len > 4 && (endsWith(s, len, "ιουσ"))) {
            len -= 4;
            removed = true;
        }

        if (removed) {                                    // like γιος -> γ
            if (endsWithVowel(s, len) || contains(exc5, s, 0, len) || len < 2) {
                len++;  // add back -ι
            } else if (endsWith(s, len, "παλ")) {
                // add -αι emoved > 4 chars so its safe)
                len += 2;
                s.setCharAt(len - 2, 'α');
                s.setCharAt(len - 1, 'ι');
            }
        }
        return len;
    }

    private static final Set<String> exc6 = new HashSet<String>(
            Arrays.asList("αδ", "αλ", "αμαν", "αμερ", "αμμοχαλ", "ανηθ", "αντιδ",
                    "απλ", "αττ", "αφρ", "βασ", "βρωμ", "βρωμ", "γεν", "γερ", "δ", "δυτ",
                    "ειδ", "ενδ", "εξωδ", "ηθ", "θετ", "καλλιν", "καλπ", "καταδ", "κρ",
                    "κωδ", "λογ", "μ", "μερ", "μοναδ", "μουλ", "μουσ", "μπαγιατ",
                    "μπαν", "μπολ", "μποσ", "μυστ", "ν", "νιτ", "ξικ", "οπτ", "παν",
                    "πετσ", "πικαντ", "πιτσ", "πλαστ", "πλιατσ", "ποντ", "ποστελν",
                    "πρωτοδ", "σερτ", "σημαντ", "στατ", "συναδ", "συνομηλ", "τελ", "τεχν",
                    "τροπ", "τσαμ", "υποδ", "φ", "φιλον", "φυλοδ", "φυσ", "χασ")
    );

    private int rule6(StringBuilder s, int len) {
        boolean removed = false;
        if (len > 3 && (endsWith(s, len, "ικα")
                || endsWith(s, len, "ικο")
                || endsWith(s, len, "ικη"))) {
            len -= 3;
            removed = true;
        } else if (len > 4 && (endsWith(s, len, "ικου")
                || endsWith(s, len, "ικων")
                || endsWith(s, len, "ικωσ")
                || endsWith(s, len, "ικοσ")
                || endsWith(s, len, "ικον")
                || endsWith(s, len, "ικοι")
                || endsWith(s, len, "ικησ")
                || endsWith(s, len, "ικεσ"))) {
            len -= 4;
            removed = true;
        } else if (len > 5 && (endsWith(s, len, "ικουσ")
                || endsWith(s, len, "ικεισ"))) {
            len -= 5;
            removed = true;
        }

        if (removed) {
            if (endsWithVowel(s, len)
                    || contains(exc6, s, 0, len)
                    || endsWith(s, len, "φοιν")) {
                len += 2; // add back -ικ
            }
        }
        return len;
    }

    private static final Set<String> exc7 = new HashSet<String>(
            Arrays.asList("αναπ", "αποθ", "αποκ", "αποστ", "βουβ", "ξεθ", "ουλ",
                    "πεθ", "πικρ", "ποτ", "σιχ", "χ")
    );

    private int rule7(StringBuilder s, int len) {
        if (len == 5 && endsWith(s, len, "αγαμε")) {
            return len - 1;
        }

        if (len > 7 && endsWith(s, len, "ηθηκαμε")) {
            len -= 7;
        } else if (len > 6 && endsWith(s, len, "ουσαμε")) {
            len -= 6;
        } else if (len > 5 && (endsWith(s, len, "αγαμε")
                || endsWith(s, len, "ησαμε")
                || endsWith(s, len, "ηκαμε"))) {
            len -= 5;
        }

        if (len > 3 && endsWith(s, len, "αμε")) {
            len -= 3;
            if (contains(exc7, s, 0, len)) {
                len += 2; // add back -αμ
            }
        }

        return len;
    }

    private static final Set<String> exc8a = new HashSet<String>(
            Arrays.asList("τρ", "τσ")
    );

    private static final Set<String> exc8b = new HashSet<String>(
            Arrays.asList("βετερ", "βουλκ", "βραχμ", "γ", "δραδουμ", "θ", "καλπουζ",
                    "καστελ", "κορμορ", "λαοπλ", "μωαμεθ", "μ", "μουσουλμ", "ν", "ουλ",
                    "π", "πελεκ", "πλ", "πολισ", "πορτολ", "σαρακατσ", "σουλτ",
                    "τσαρλατ", "ορφ", "τσιγγ", "τσοπ", "φωτοστεφ", "χ", "ψυχοπλ", "αγ",
                    "ορφ", "γαλ", "γερ", "δεκ", "διπλ", "αμερικαν", "ουρ", "πιθ",
                    "πουριτ", "σ", "ζωντ", "ικ", "καστ", "κοπ", "λιχ", "λουθηρ", "μαιντ",
                    "μελ", "σιγ", "σπ", "στεγ", "τραγ", "τσαγ", "φ", "ερ", "αδαπ",
                    "αθιγγ", "αμηχ", "ανικ", "ανοργ", "απηγ", "απιθ", "ατσιγγ", "βασ",
                    "βασκ", "βαθυγαλ", "βιομηχ", "βραχυκ", "διατ", "διαφ", "ενοργ",
                    "θυσ", "καπνοβιομηχ", "καταγαλ", "κλιβ", "κοιλαρφ", "λιβ",
                    "μεγλοβιομηχ", "μικροβιομηχ", "νταβ", "ξηροκλιβ", "ολιγοδαμ",
                    "ολογαλ", "πενταρφ", "περηφ", "περιτρ", "πλατ", "πολυδαπ", "πολυμηχ",
                    "στεφ", "ταβ", "τετ", "υπερηφ", "υποκοπ", "χαμηλοδαπ", "ψηλοταβ")
    );

    private int rule8(StringBuilder s, int len) {
        boolean removed = false;

        if (len > 8 && endsWith(s, len, "ιουντανε")) {
            len -= 8;
            removed = true;
        } else if (len > 7 && endsWith(s, len, "ιοντανε")
                || endsWith(s, len, "ουντανε")
                || endsWith(s, len, "ηθηκανε")) {
            len -= 7;
            removed = true;
        } else if (len > 6 && endsWith(s, len, "ιοτανε")
                || endsWith(s, len, "οντανε")
                || endsWith(s, len, "ουσανε")) {
            len -= 6;
            removed = true;
        } else if (len > 5 && endsWith(s, len, "αγανε")
                || endsWith(s, len, "ησανε")
                || endsWith(s, len, "οτανε")
                || endsWith(s, len, "ηκανε")) {
            len -= 5;
            removed = true;
        }

        if (removed && contains(exc8a, s, 0, len)) {
            // add -αγαν (we removed > 4 chars so its safe)
            len += 4;
            s.setCharAt(len - 4, 'α');
            s.setCharAt(len - 3, 'γ');
            s.setCharAt(len - 2, 'α');
            s.setCharAt(len - 1, 'ν');
        }

        if (len > 3 && endsWith(s, len, "ανε")) {
            len -= 3;
            if (endsWithVowelNoY(s, len) || contains(exc8b, s, 0, len)) {
                len += 2; // add back -αν
            }
        }

        return len;
    }

    private static final Set<String> exc9 = new HashSet<String>(
            Arrays.asList("αβαρ", "βεν", "εναρ", "αβρ", "αδ", "αθ", "αν", "απλ",
                    "βαρον", "ντρ", "σκ", "κοπ", "μπορ", "νιφ", "παγ", "παρακαλ", "σερπ",
                    "σκελ", "συρφ", "τοκ", "υ", "δ", "εμ", "θαρρ", "θ")
    );

    private int rule9(StringBuilder s, int len) {
        if (len > 5 && endsWith(s, len, "ησετε")) {
            len -= 5;
        }

        if (len > 3 && endsWith(s, len, "ετε")) {
            len -= 3;
            if (contains(exc9, s, 0, len)
                    || endsWithVowelNoY(s, len)
                    || endsWith(s, len, "οδ")
                    || endsWith(s, len, "αιρ")
                    || endsWith(s, len, "φορ")
                    || endsWith(s, len, "ταθ")
                    || endsWith(s, len, "διαθ")
                    || endsWith(s, len, "σχ")
                    || endsWith(s, len, "ενδ")
                    || endsWith(s, len, "ευρ")
                    || endsWith(s, len, "τιθ")
                    || endsWith(s, len, "υπερθ")
                    || endsWith(s, len, "ραθ")
                    || endsWith(s, len, "ενθ")
                    || endsWith(s, len, "ροθ")
                    || endsWith(s, len, "σθ")
                    || endsWith(s, len, "πυρ")
                    || endsWith(s, len, "αιν")
                    || endsWith(s, len, "συνδ")
                    || endsWith(s, len, "συν")
                    || endsWith(s, len, "συνθ")
                    || endsWith(s, len, "χωρ")
                    || endsWith(s, len, "πον")
                    || endsWith(s, len, "βρ")
                    || endsWith(s, len, "καθ")
                    || endsWith(s, len, "ευθ")
                    || endsWith(s, len, "εκθ")
                    || endsWith(s, len, "νετ")
                    || endsWith(s, len, "ρον")
                    || endsWith(s, len, "αρκ")
                    || endsWith(s, len, "βαρ")
                    || endsWith(s, len, "βολ")
                    || endsWith(s, len, "ωφελ")) {
                len += 2; // add back -ετ
            }
        }

        return len;
    }

    private int rule10(StringBuilder s, int len) {
        if (len > 5 && (endsWith(s, len, "οντασ") || endsWith(s, len, "ωντασ"))) {
            len -= 5;
            if (len == 3 && endsWith(s, len, "αρχ")) {
                len += 3; // add back *ντ
                s.setCharAt(len - 3, 'ο');
            }
            if (endsWith(s, len, "κρε")) {
                len += 3; // add back *ντ
                s.setCharAt(len - 3, 'ω');
            }
        }

        return len;
    }

    private int rule11(StringBuilder s, int len) {
        if (len > 6 && endsWith(s, len, "ομαστε")) {
            len -= 6;
            if (len == 2 && endsWith(s, len, "ον")) {
                len += 5; // add back -ομαστ
            }
        } else if (len > 7 && endsWith(s, len, "ιομαστε")) {
            len -= 7;
            if (len == 2 && endsWith(s, len, "ον")) {
                len += 5;
                s.setCharAt(len - 5, 'ο');
                s.setCharAt(len - 4, 'μ');
                s.setCharAt(len - 3, 'α');
                s.setCharAt(len - 2, 'σ');
                s.setCharAt(len - 1, 'τ');
            }
        }
        return len;
    }

    private static final Set<String> exc12a = new HashSet<String>(
            Arrays.asList("π", "απ", "συμπ", "ασυμπ", "ακαταπ", "αμεταμφ"));

    private static final Set<String> exc12b = new HashSet<String>(
            Arrays.asList("αλ", "αρ", "εκτελ", "ζ", "μ", "ξ", "παρακαλ", "αρ", "προ", "νισ"));

    private int rule12(StringBuilder s, int len) {
        if (len > 5 && endsWith(s, len, "ιεστε")) {
            len -= 5;
            if (contains(exc12a, s, 0, len)) {
                len += 4; // add back -ιεστ
            }
        }

        if (len > 4 && endsWith(s, len, "εστε")) {
            len -= 4;
            if (contains(exc12b, s, 0, len)) {
                len += 3; // add back -εστ
            }
        }

        return len;
    }

    private static final Set<String> exc13 = new HashSet<String>(
            Arrays.asList("διαθ", "θ", "παρακαταθ", "προσθ", "συνθ")
    );

    private int rule13(StringBuilder s, int len) {
        if (len > 6 && endsWith(s, len, "ηθηκεσ")) {
            len -= 6;
        } else if (len > 5 && (endsWith(s, len, "ηθηκα") || endsWith(s, len, "ηθηκε"))) {
            len -= 5;
        }

        boolean removed = false;

        if (len > 4 && endsWith(s, len, "ηκεσ")) {
            len -= 4;
            removed = true;
        } else if (len > 3 && (endsWith(s, len, "ηκα") || endsWith(s, len, "ηκε"))) {
            len -= 3;
            removed = true;
        }

        if (removed && (contains(exc13, s, 0, len)
                || endsWith(s, len, "σκωλ")
                || endsWith(s, len, "σκουλ")
                || endsWith(s, len, "ναρθ")
                || endsWith(s, len, "σφ")
                || endsWith(s, len, "οθ")
                || endsWith(s, len, "πιθ"))) {
            len += 2; // add back the -ηκ
        }

        return len;
    }

    private static final Set<String> exc14 = new HashSet<String>(
            Arrays.asList("φαρμακ", "χαδ", "αγκ", "αναρρ", "βρομ", "εκλιπ", "λαμπιδ",
                    "λεχ", "μ", "πατ", "ρ", "λ", "μεδ", "μεσαζ", "υποτειν", "αμ", "αιθ",
                    "ανηκ", "δεσποζ", "ενδιαφερ", "δε", "δευτερευ", "καθαρευ", "πλε",
                    "τσα")
    );

    private int rule14(StringBuilder s, int len) {
        boolean removed = false;

        if (len > 5 && endsWith(s, len, "ουσεσ")) {
            len -= 5;
            removed = true;
        } else if (len > 4 && (endsWith(s, len, "ουσα") || endsWith(s, len, "ουσε"))) {
            len -= 4;
            removed = true;
        }

        if (removed && (contains(exc14, s, 0, len)
                || endsWithVowel(s, len)
                || endsWith(s, len, "ποδαρ")
                || endsWith(s, len, "βλεπ")
                || endsWith(s, len, "πανταχ")
                || endsWith(s, len, "φρυδ")
                || endsWith(s, len, "μαντιλ")
                || endsWith(s, len, "μαλλ")
                || endsWith(s, len, "κυματ")
                || endsWith(s, len, "λαχ")
                || endsWith(s, len, "ληγ")
                || endsWith(s, len, "φαγ")
                || endsWith(s, len, "ομ")
                || endsWith(s, len, "πρωτ"))) {
            len += 3; // add back -ουσ
        }

        return len;
    }

    private static final Set<String> exc15a = new HashSet<String>(
            Arrays.asList("αβαστ", "πολυφ", "αδηφ", "παμφ", "ρ", "ασπ", "αφ", "αμαλ",
                    "αμαλλι", "ανυστ", "απερ", "ασπαρ", "αχαρ", "δερβεν", "δροσοπ",
                    "ξεφ", "νεοπ", "νομοτ", "ολοπ", "ομοτ", "προστ", "προσωποπ", "συμπ",
                    "συντ", "τ", "υποτ", "χαρ", "αειπ", "αιμοστ", "ανυπ", "αποτ",
                    "αρτιπ", "διατ", "εν", "επιτ", "κροκαλοπ", "σιδηροπ", "λ", "ναυ",
                    "ουλαμ", "ουρ", "π", "τρ", "μ")
    );

    private static final Set<String> exc15b = new HashSet<String>(
            Arrays.asList("ψοφ", "ναυλοχ")
    );

    private int rule15(StringBuilder s, int len) {
        boolean removed = false;
        if (len > 4 && endsWith(s, len, "αγεσ")) {
            len -= 4;
            removed = true;
        } else if (len > 3 && (endsWith(s, len, "αγα") || endsWith(s, len, "αγε"))) {
            len -= 3;
            removed = true;
        }

        if (removed) {
            final boolean cond1 = contains(exc15a, s, 0, len)
                    || endsWith(s, len, "οφ")
                    || endsWith(s, len, "πελ")
                    || endsWith(s, len, "χορτ")
                    || endsWith(s, len, "λλ")
                    || endsWith(s, len, "σφ")
                    || endsWith(s, len, "ρπ")
                    || endsWith(s, len, "φρ")
                    || endsWith(s, len, "πρ")
                    || endsWith(s, len, "λοχ")
                    || endsWith(s, len, "σμην");

            final boolean cond2 = contains(exc15b, s, 0, len)
                    || endsWith(s, len, "κολλ");

            if (cond1 && !cond2) {
                len += 2; // add back -αγ
            }
        }

        return len;
    }

    private static final Set<String> exc16 = new HashSet<String>(
            Arrays.asList("ν", "χερσον", "δωδεκαν", "ερημον", "μεγαλον", "επταν", "ι")
    );

    private int rule16(StringBuilder s, int len) {
        boolean removed = false;
        if (len > 4 && endsWith(s, len, "ησου")) {
            len -= 4;
            removed = true;
        } else if (len > 3 && (endsWith(s, len, "ησε") || endsWith(s, len, "ησα"))) {
            len -= 3;
            removed = true;
        }

        if (removed && contains(exc16, s, 0, len)) {
            len += 2; // add back -ησ
        }
        return len;
    }

    private static final Set<String> exc17 = new HashSet<String>(
            Arrays.asList("ασβ", "σβ", "αχρ", "χρ", "απλ", "αειμν", "δυσχρ", "ευχρ", "κοινοχρ", "παλιμψ")
    );

    private int rule17(StringBuilder s, int len) {
        if (len > 4 && endsWith(s, len, "ηστε")) {
            len -= 4;
            if (contains(exc17, s, 0, len)) {
                len += 3; // add back the -ηστ
            }
        }

        return len;
    }

    private static final Set<String> exc18 = new HashSet<String>(
            Arrays.asList("ν", "ρ", "σπι", "στραβομουτσ", "κακομουτσ", "εξων")
    );

    private int rule18(StringBuilder s, int len) {
        boolean removed = false;

        if (len > 6 && (endsWith(s, len, "ησουνε") || endsWith(s, len, "ηθουνε"))) {
            len -= 6;
            removed = true;
        } else if (len > 4 && endsWith(s, len, "ουνε")) {
            len -= 4;
            removed = true;
        }

        if (removed && contains(exc18, s, 0, len)) {
            len += 3;
            s.setCharAt(len - 3, 'ο');
            s.setCharAt(len - 2, 'υ');
            s.setCharAt(len - 1, 'ν');
        }
        return len;
    }

    private static final Set<String> exc19 = new HashSet<String>(
            Arrays.asList("παρασουσ", "φ", "χ", "ωριοπλ", "αζ", "αλλοσουσ", "ασουσ")
    );

    private int rule19(StringBuilder s, int len) {
        boolean removed = false;

        if (len > 6 && (endsWith(s, len, "ησουμε") || endsWith(s, len, "ηθουμε"))) {
            len -= 6;
            removed = true;
        } else if (len > 4 && endsWith(s, len, "ουμε")) {
            len -= 4;
            removed = true;
        }

        if (removed && contains(exc19, s, 0, len)) {
            len += 3;
            s.setCharAt(len - 3, 'ο');
            s.setCharAt(len - 2, 'υ');
            s.setCharAt(len - 1, 'μ');
        }
        return len;
    }

    private static final Set<String> exc20a = new HashSet<String>(
            Arrays.asList("γραμμ")
    );

    private static final Set<String> exc20b = new HashSet<String>(
            Arrays.asList("γεμ", "σταμ")
    );

    private int rule20(StringBuilder s, int len) {
        boolean removed = false;
        if (len > 6 && endsWith(s, len, "ματουσ")) {
            len -= 5;
            removed = true;
        } else if (len > 5 && (endsWith(s, len, "ματων")
                || endsWith(s, len, "ματοσ")
                || endsWith(s, len, "ματωσ")
                || endsWith(s, len, "ματου")
                || endsWith(s, len, "ματησ")
                || endsWith(s, len, "ματεσ")
                || endsWith(s, len, "ματοι"))) {
            len -= 4;
            removed = true;
        } else if (len > 4 && endsWith(s, len, "ματα")
                || endsWith(s, len, "ματο")
                || endsWith(s, len, "ματη")) {
            len -= 3;
            removed = true;
        }

        if (removed) {
            if (contains(exc20a, s, 0, len)) {
                // add -α
                len += 1;
                s.setCharAt(len - 1, 'α');
            } else if (contains(exc20b, s, 0, len)) {
                len += 2; // add -ατ
            }
        }

        return len;
    }

    private int rule21(StringBuilder s, int len) {
        if (len > 9 && endsWith(s, len, "ιοντουσαν")) {
            return len - 9;
        }

        if (len > 8 && (endsWith(s, len, "ιομασταν")
                || endsWith(s, len, "ιοσασταν")
                || endsWith(s, len, "ιουμαστε")
                || endsWith(s, len, "οντουσαν"))) {
            return len - 8;
        }

        if (len > 7 && (endsWith(s, len, "ιεμαστε")
                || endsWith(s, len, "ιεσαστε")
                || endsWith(s, len, "ιομουνα")
                || endsWith(s, len, "ιοσαστε")
                || endsWith(s, len, "ιοσουνα")
                || endsWith(s, len, "ιουνται")
                || endsWith(s, len, "ιουνταν")
                || endsWith(s, len, "ηθηκατε")
                || endsWith(s, len, "ομασταν")
                || endsWith(s, len, "οσασταν")
                || endsWith(s, len, "ουμαστε"))) {
            return len - 7;
        }

        if (len > 6 && (endsWith(s, len, "ιομουν")
                || endsWith(s, len, "ιονταν")
                || endsWith(s, len, "ιοσουν")
                || endsWith(s, len, "ηθειτε")
                || endsWith(s, len, "ηθηκαν")
                || endsWith(s, len, "ομουνα")
                || endsWith(s, len, "οσαστε")
                || endsWith(s, len, "οσουνα")
                || endsWith(s, len, "ουνται")
                || endsWith(s, len, "ουνταν")
                || endsWith(s, len, "ουσατε"))) {
            return len - 6;
        }

        if (len > 5 && (endsWith(s, len, "αγατε")
                || endsWith(s, len, "ιεμαι")
                || endsWith(s, len, "ιεται")
                || endsWith(s, len, "ιεσαι")
                || endsWith(s, len, "ιοταν")
                || endsWith(s, len, "ιουμα")
                || endsWith(s, len, "ηθεισ")
                || endsWith(s, len, "ηθουν")
                || endsWith(s, len, "ηκατε")
                || endsWith(s, len, "ησατε")
                || endsWith(s, len, "ησουν")
                || endsWith(s, len, "ομουν")
                || endsWith(s, len, "ονται")
                || endsWith(s, len, "ονταν")
                || endsWith(s, len, "οσουν")
                || endsWith(s, len, "ουμαι")
                || endsWith(s, len, "ουσαν"))) {
            return len - 5;
        }

        if (len > 4 && (endsWith(s, len, "αγαν")
                || endsWith(s, len, "αμαι")
                || endsWith(s, len, "ασαι")
                || endsWith(s, len, "αται")
                || endsWith(s, len, "ειτε")
                || endsWith(s, len, "εσαι")
                || endsWith(s, len, "εται")
                || endsWith(s, len, "ηδεσ")
                || endsWith(s, len, "ηδων")
                || endsWith(s, len, "ηθει")
                || endsWith(s, len, "ηκαν")
                || endsWith(s, len, "ησαν")
                || endsWith(s, len, "ησει")
                || endsWith(s, len, "ησεσ")
                || endsWith(s, len, "ομαι")
                || endsWith(s, len, "οταν"))) {
            return len - 4;
        }

        if (len > 3 && (endsWith(s, len, "αει")
                || endsWith(s, len, "εισ")
                || endsWith(s, len, "ηθω")
                || endsWith(s, len, "ησω")
                || endsWith(s, len, "ουν")
                || endsWith(s, len, "οισ")
                || endsWith(s, len, "ουσ"))) {
            return len - 3;
        }

        if (len > 2 && (endsWith(s, len, "αν")
                || endsWith(s, len, "ασ")
                || endsWith(s, len, "αω")
                || endsWith(s, len, "ει")
                || endsWith(s, len, "εσ")
                || endsWith(s, len, "ησ")
                || endsWith(s, len, "οι")
                || endsWith(s, len, "οσ")
                || endsWith(s, len, "ου")
                || endsWith(s, len, "υσ")
                || endsWith(s, len, "ων"))) {
            return len - 2;
        }

        if (len > 1 && endsWithVowel(s, len)) {
            return len - 1;
        }

        return len;
    }

    private static final Set<String> exc22a = new HashSet<String>(
            Arrays.asList("εξ", "εσ", "κατ", "αν", "κ", "μ", "πρ"));

    private static final Set<String> exc22b = new HashSet<String>(
            Arrays.asList("κα", "μ", "λε", "ελε", "δε"));

    private int rule22(StringBuilder s, int len) {
        boolean removed = false;
        if (endsWith(s, len, "εστερ")
                || endsWith(s, len, "εστατ")) {
            return len - 5;
        }

        if (endsWith(s, len, "οτερ")
                || endsWith(s, len, "οτατ")
                || endsWith(s, len, "υτερ")
                || endsWith(s, len, "υτατ")
                || endsWith(s, len, "ωτερ")
                || endsWith(s, len, "ωτατ")) {
            len -= 4;
            removed = true;
        }

        if (removed) {
            if (contains(exc22a, s, 0, len)) {
                len += 4;
            } else if (contains(exc22b, s, 0, len)) {
                len += 2;
                s.setCharAt(len - 2, 'υ');
                s.setCharAt(len - 1, 'τ');
            }
            return len;
        }

        return len;
    }

    private boolean endsWith(StringBuilder s, int len, String suffix) {
        final int suffixLen = suffix.length();
        if (suffixLen > len) {
            return false;
        }
        for (int i = suffixLen - 1; i >= 0; i--) {
            if (s.charAt(len - (suffixLen - i)) != suffix.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    private boolean endsWithVowel(StringBuilder s, int len) {
        if (len == 0) {
            return false;
        }
        switch (s.charAt(len - 1)) {
            case 'α':
            case 'ε':
            case 'η':
            case 'ι':
            case 'ο':
            case 'υ':
            case 'ω':
                return true;
            default:
                return false;
        }
    }

    private boolean endsWithVowelNoY(StringBuilder s, int len) {
        if (len == 0) {
            return false;
        }
        switch (s.charAt(len - 1)) {
            case 'α':
            case 'ε':
            case 'η':
            case 'ι':
            case 'ο':
            case 'ω':
                return true;
            default:
                return false;
        }
    }

    private boolean contains(Set<String> ssHaystack, StringBuilder sNeedle, int iStart, int iEnd) {
        return ssHaystack.contains(sNeedle.substring(iStart, iEnd));
    }

}
