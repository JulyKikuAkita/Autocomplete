package lyft.autocomplete;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java
 */
public class FileUtil {
    List<String> badWords = new LinkedList<>();

    public void readFileWordRankToTrie(Trie trie, String path) {
        String line = null;
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            int rank = 0;
            while ((line = br.readLine()) != null) {
                String letters = examineLine(line.toCharArray()); //trick have shit lyft
                trie.insert(new WordRank(letters, ++rank));
            }
            //int sz = badWords.size();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // same as line.trim().replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll(".", "");
    public String examineLine(char[] chars) {
        StringBuilder sb = new StringBuilder();
        for (char c :  chars) {
            if (Character.isLetter(c)) {
                sb.append(c);
            } else {
                badWords.add(new String(chars));
                break;
            }
        }
        return sb.toString().toLowerCase();
    }

    public void readFilePrefixToTrieAndGenerateOutputFile(Trie trie, String input, String output) {
        String line = null;
        try {
            File file = new File(input);
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                line.trim();
                List<WordRank> res = trie.getAutoRecommendWord(line, 5);
                //System.out.println(line +" ");
                String[] prefix = new String[]{'\n' +line + ": "};
                generateOutputFile(output, Arrays.asList(prefix));
                if (res.isEmpty()) {
                    generateOutputFile(output, Arrays.asList(new String[]{"No Result"}));
                } else {
                    for (WordRank wr : res) {
                        //System.out.println(line +" " + wr.toString());
                        String[] row = new String[]{wr.toString()};
                        generateOutputFile(output, Arrays.asList(row));
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateOutputFile(String filename, List<String> lines) {
        Path parent = Paths.get("src/main/res/dict.txt").getParent();
        String absoPath = parent.toString()+ "/" + filename;
        Charset utf8 = StandardCharsets.UTF_8;
        try {
            Files.write(Paths.get(absoPath)
                    , lines
                    , utf8
                    , StandardOpenOption.CREATE
                    , StandardOpenOption.APPEND); //overwrite existing content -TRUNCATE_EXISTING
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

