package lyft.autocomplete;

import org.testng.annotations.Test;

import java.util.List;

/**
 *
 */
public class TestAutocomplete {
    @Test
    public void testTrieMatchPrefix() throws Exception {
        Trie trie = new Trie();
        FileUtil fileUtil = new FileUtil();
        fileUtil.readFileWordRankToTrie(trie, "src/main/res/dict.txt");
        fileUtil.readFilePrefixToTrieAndGenerateOutputFile(trie, "src/main/res/autoInput.txt", "test1.out");
    }

    @Test
    public void testTrieMatchPrefixUnit() throws Exception {
        Trie trie = new Trie();
        trie.insert(new WordRank("t", 1));
        trie.insert(new WordRank("th", 2));
        trie.insert(new WordRank("the", 3));
        trie.insert(new WordRank("ttp", 4));
        trie.insert(new WordRank("tpe", 5));
        trie.insert(new WordRank("teet", 6));
        List<WordRank> res = trie.getAutoRecommendWord("th", 5);
        for (WordRank wr: res) {
            System.out.println(wr.toString());
        }
    }

    @Test
    public void testTrieMatchPrefixUnit2() throws Exception {
        Trie trie = new Trie();
        FileUtil fileUtil = new FileUtil();
        fileUtil.readFileWordRankToTrie(trie, "src/main/res/dict.txt");

        List<WordRank> res = trie.getAutoRecommendWord("th", 5);
        System.out.println(res.toString());
        for (WordRank wr: res) {
            System.out.println(wr.toString());
        }
    }
}