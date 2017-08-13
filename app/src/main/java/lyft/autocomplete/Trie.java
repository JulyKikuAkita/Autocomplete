package lyft.autocomplete;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * http://www.geeksforgeeks.org/trie-insert-and-search/
 */
public class Trie {
    private static final int L = 26;
    private TrieNode mRoot;

    public Trie() {
        mRoot = new TrieNode();
    }

    public void insert(WordRank wordRank) {
        TrieNode cur = mRoot;
        if (wordRank != null && wordRank.mWord != null) {
            for (char c : wordRank.mWord.toCharArray()) {
                if (cur.mChild[c - 'a'] == null) {
                    cur.mChild[c - 'a'] = new TrieNode();
                }
                cur = cur.mChild[c - 'a'];
            }
            cur.mIsWord = true;
            cur.mWorkRank = new WordRank(wordRank.mWord, wordRank.mRank);
            cur.mWord = wordRank.mWord;
            cur.mRank = wordRank.mRank;
        }
    }

    private TrieNode search(String prefix) {
        TrieNode cur = mRoot;
        for (char c : prefix.toCharArray()) {
            if (cur.mChild[c - 'a'] == null) {
                return null;
            }
            cur = cur.mChild[c - 'a'];
        }
        return cur;
    }

    public List<WordRank> getAutoRecommendWord(String prefix, int k) {
        TrieNode cur = search(prefix);
        List<WordRank> res = new ArrayList<>();
        PriorityQueue<WordRank> pq = new PriorityQueue<>(
                (WordRank a, WordRank b) -> (a.mRank - b.mRank)); //asec

        if (cur != null){
            //dfs find all match by key
            dfs(cur, pq);
            for(int i = 0; i < k ; i++) {
                if (!pq.isEmpty()) {
                    //System.out.println(pq.peek().mWord);
                    res.add(pq.poll());
                }
            }
        }
        return res;
    }

    private void dfs(TrieNode node, PriorityQueue<WordRank> pq) {
        if (node == null || node.mWorkRank == null || node.mWorkRank.mRank == 0) return ;
        if (node.mIsWord) {
            pq.add(new WordRank(node.mWord, node.mRank)); //cannot return, mWord might not be leaf
        }
        for (int i = 0; i < L; i++) {
            if (node.mChild[i] != null) {
                dfs(node.mChild[i], pq);
            }
        }
    }

    private class TrieNode{
        TrieNode[] mChild;
        boolean mIsWord;
        WordRank mWorkRank;
        String mWord;
        int mRank;

        public TrieNode() {
            mChild = new TrieNode[L];
            mIsWord = false;
            mWorkRank = null;
            mRank = -1;
            mWord = null;

        }
    }
}
