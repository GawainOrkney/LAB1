import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Lab1 {

    static HashMap<Character, Character> my_key;

    public static void main(String[] args) {

        String original;
        String pathname = "Text/OriginalText.txt";
        String line;
        original = new String();

        try {

            File file = new File(pathname);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            line = reader.readLine();

            while (line != null) {

                line += '\n';
                original += line;
                line = reader.readLine();
            }

        } catch (Exception err) {
            err.printStackTrace();
        }

        String text = encryption(6, "комар", original);

        analysis_monogram(text, original);
        analysis_bigram(text, original);
    }

    private static void analysis_bigram(String original, String text) {

        HashMap<String, Double> bigram_text_frecuency10 = new HashMap<>();
        HashMap<String, Double> bigram_original_frecuency10 = new HashMap<>();

        HashMap<String, Integer> text_value = new HashMap<>();
        int count_bigrams = 0;

        for(int i = 0; i < text.length() - 1; i++) {

            if (!((text.charAt(i) >= 'а' && text.charAt(i) <= 'я') || (text.charAt(i) >= 'А' && text.charAt(i) <= 'Я')))
                continue;

            if (!((text.charAt(i + 1) >= 'а' && text.charAt(i + 1) <= 'я') || (text.charAt(i + 1) >= 'А' && text.charAt(i + 1) <= 'Я')))
                continue;

            char sym1 = (text.charAt(i) >= 'А' && text.charAt(i) <= 'Я') ? (char) (text.charAt(i) + ('а' - 'А')) : text.charAt(i);
            char sym2 =(text.charAt(i+1) >= 'А' && text.charAt(i+1) <= 'Я') ? (char) (text.charAt(i+1) + ('а' - 'А')) : text.charAt(i+1);

            if (!text_value.containsKey("" + sym1 + sym2)) {

                text_value.put("" + sym1 + sym2, 1);
                count_bigrams++;
            } else {

                text_value.put("" + sym1 + sym2,
                        text_value.get("" + sym1 + sym2) + 1);
                count_bigrams++;
            }
        }

        for (int i = 0; i < 10; i++) {

            String max_key = "";
            int max = 0;

            for (String key : text_value.keySet()) {

                if (text_value.get(key) > max) {

                    max_key = key;
                    max = text_value.get(key);
                }
            }

            text_value.remove(max_key);
            bigram_text_frecuency10.put(max_key, max * 100 / (double) count_bigrams);
        }

        HashMap<String, Integer> original_value = new HashMap<>();
        count_bigrams = 0;

        for(int i = 0; i < original.length() - 1; i++) {

            if (!((original.charAt(i) >= 'а' && original.charAt(i) <= 'я') || (original.charAt(i) >= 'А' && original.charAt(i) <= 'Я')))
                continue;

            if (!((original.charAt(i + 1) >= 'а' && original.charAt(i + 1) <= 'я') || (original.charAt(i + 1) >= 'А' && original.charAt(i + 1) <= 'Я')))
                continue;

            char sym1 = (original.charAt(i) >= 'А' && original.charAt(i) <= 'Я') ? (char) (original.charAt(i) + ('а' - 'А')) : original.charAt(i);
            char sym2 =(original.charAt(i+1) >= 'А' && original.charAt(i+1) <= 'Я') ? (char) (original.charAt(i+1) + ('а' - 'А')) : original.charAt(i+1);

            if (!original_value.containsKey("" + sym1 + sym2)) {

                original_value.put("" + sym1 + sym2, 1);
                count_bigrams++;
            } else {

                original_value.put("" + sym1 + sym2,
                        original_value.get("" + sym1 + sym2) + 1);
                count_bigrams++;
            }
        }

        for (int i = 0; i < 5; i++) {

            String max_key = "";
            int max = 0;

            for (String key : original_value.keySet()) {

                if (original_value.get(key) > max) {

                    max_key = key;
                    max = original_value.get(key);
                }
            }

            original_value.remove(max_key);
            bigram_original_frecuency10.put(max_key, max * 100 / (double) count_bigrams);
        }

        HashMap<String, String> bigram_map = new HashMap<>();

        //Сопаставление

        for (String key_original: bigram_original_frecuency10.keySet()) {

            double diff = 100;
            String key_encr = "";

            for (String key_text: bigram_text_frecuency10.keySet()) {

                if (Math.abs(bigram_text_frecuency10.get(key_text) - bigram_original_frecuency10.get(key_original)) < diff) {

                    diff = Math.abs(bigram_text_frecuency10.get(key_text) - bigram_original_frecuency10.get(key_original));
                    key_encr = key_text;
                }
            }

            bigram_map.put(key_original, key_encr);
        }

        //Расшифровка

        for (String key: bigram_map.keySet()) {


            char let1 = key.charAt(0);
            char let2 = bigram_map.get(key).charAt(0);

            if (my_key.get(let1) != let2) {

                char let = my_key.get(let1);
                my_key.put(let1, let2);

                for (char ch: my_key.keySet()) {

                    if (my_key.get(ch) == let2 && ch != let1) {
                        my_key.put(ch, let);
                        break;
                    }
                }
            }

            let1 = key.charAt(1);
            let2 = bigram_map.get(key).charAt(1);

            if (my_key.get(let1) != let2) {

                char let = my_key.get(let1);
                my_key.put(let1, let2);

                for (char ch: my_key.keySet()) {

                    if (my_key.get(ch) == let2 && ch != let1) {
                        my_key.put(ch, let);
                        break;
                    }
                }
            }
        }

        String new_text = "";

        for (int i = 0; i < original.length(); i++) {

            if ((original.charAt(i) >= 'а' && original.charAt(i) <= 'я')) {

                new_text += my_key.get(original.charAt(i));
            } else if ((original.charAt(i) >= 'А' && original.charAt(i) <= 'Я')) {

                char sym = my_key.get((char) (original.charAt(i) + ('а'-'А')));
                new_text += (char) (sym - ('а'-'А'));
            } else {

                new_text += original.charAt(i);
            }
        }

        System.out.println("\n\n\n\nРасшифровка по биграммам:\n\n\n\n" + new_text);
    }

    static void analysis_monogram(String original, String text) {

        HashMap<Character, Double> frequency = new HashMap<>();
        HashMap<Character, Double> frequency_original = new HashMap<>();


        HashMap<Character, Integer> text_value = new HashMap<>();
        int count_letter = 0;

        frequency.put('а', 8.01);
        frequency.put('б', 1.59);
        frequency.put('в', 4.54);
        frequency.put('г', 1.70);
        frequency.put('д', 2.98);
        frequency.put('е', 8.45);
        frequency.put('ж', 0.94);
        frequency.put('з', 1.65);
        frequency.put('и', 7.35);
        frequency.put('й', 1.21);
        frequency.put('к', 3.49);
        frequency.put('л', 4.40);
        frequency.put('м', 3.21);
        frequency.put('н', 6.70);
        frequency.put('о', 10.97);
        frequency.put('п', 2.81);
        frequency.put('р', 4.73);
        frequency.put('с', 5.47);
        frequency.put('т', 6.26);
        frequency.put('у', 2.62);
        frequency.put('ф', 0.26);
        frequency.put('х', 0.97);
        frequency.put('ц', 0.48);
        frequency.put('ч', 1.44);
        frequency.put('ш', 0.73);
        frequency.put('щ', 0.36);
        frequency.put('ъ', 0.04);
        frequency.put('ы', 1.90);
        frequency.put('ь', 1.74);
        frequency.put('э', 0.32);
        frequency.put('ю', 0.64);
        frequency.put('я', 2.01);


        HashMap<Character, Integer> original_value = new HashMap<>();
        count_letter = 0;

        for(int i = 0; i < original.length(); i++) {

            if ((original.charAt(i) >= 'а' && original.charAt(i) <= 'я')) {

                count_letter++;
                if (original_value.containsKey(original.charAt(i)))
                    original_value.put(original.charAt(i), original_value.get(original.charAt(i)) + 1);
                else
                    original_value.put(original.charAt(i), 1);

            } else if (original.charAt(i) >= 'А' && original.charAt(i) <= 'Я') {

                char letter = (char) (original.charAt(i) + ('а' - 'А'));

                count_letter++;
                if (original_value.containsKey(letter))
                    original_value.put(letter, original_value.get(letter) + 1);
                else
                    original_value.put(letter, 1);
            }
        }

        for (char l = 'а'; l <= 'я'; l++) {

            if (original_value.containsKey(l))
            frequency_original.put(l, (original_value.get(l) * 100.0 / count_letter));
        }


        my_key = new HashMap<>();

        for (char key_original: frequency_original.keySet()) {

            double diff = 100;
            char key_encr = ' ';

            for (char key_text: frequency.keySet()) {

                if (Math.abs(frequency.get(key_text) - frequency_original.get(key_original)) < diff) {

                    diff = Math.abs(frequency.get(key_text) - frequency_original.get(key_original));
                    key_encr = key_text;
                }
            }

            my_key.put(key_original, key_encr);
            frequency.remove(key_encr);
        }

        String new_text = "";

        for (int i = 0; i < original.length(); i++) {

            if ((original.charAt(i) >= 'а' && original.charAt(i) <= 'я')) {

                new_text += my_key.get(original.charAt(i));
            } else if ((original.charAt(i) >= 'А' && original.charAt(i) <= 'Я')) {

                char sym = my_key.get((char) (original.charAt(i) + ('а'-'А')));
                new_text += (char) (sym - ('а'-'А'));
            } else {

                new_text += original.charAt(i);
            }
        }

        System.out.println("\n\n\n\nРасшифровка по монограмам:\n\n\n\n" + new_text);
    }

    static String encryption(int shift, String word, String original) {

        char new_alphabet [] = new char[32];

        for (char s = 'а'; s <= 'я'; s++)
            new_alphabet[s - 'а'] = s;

        for (int i = 0; i < 32; i++) {
            new_alphabet[i] = (char) (new_alphabet[i] + shift);
            if (new_alphabet[i] > 'я')
                new_alphabet[i] -= 32;
        }

        for (int i = 0; i < word.length(); i++) {

            char let = new_alphabet[i];
            new_alphabet[i] = word.charAt(i);
            for (int j = i + 1; j < 32; j++)
                if (new_alphabet[j] == word.charAt(i))
                    new_alphabet[j] = let;
        }

        for (int i = 0; i < 32; i++)
            System.out.println("" + ((char) (i + 'а')) + " = " + new_alphabet[i]);

        String new_text = "";


        for (int i = 0; i < original.length(); i++) {

            if ((original.charAt(i) >= 'а' && original.charAt(i) <= 'я')) {

                new_text += new_alphabet[original.charAt(i) - 'а'];
            } else if ((original.charAt(i) >= 'А' && original.charAt(i) <= 'Я')) {

                new_text += (char) (new_alphabet[original.charAt(i) - 'а' + ('а'-'А')] - ('а'-'А'));
            } else {

                new_text += original.charAt(i);
            }
        }

        return new_text;
    }
}
