package Compression.StanderdHuffman;

import java.util.*;

public class StandardHuffmanTree {
    StandardHuffmanNode root;
    Map<Character, String> codeTable;
    String data;
    public StandardHuffmanTree(String data){
        this.data = data;
        root = null;
        codeTable = new HashMap<>();
    }

    private void buildStandardHuffmanTree(){
        Map<Character, Integer> freq = new HashMap<>();
        for(int i = 0; i < data.length(); i++){
            if(freq.get(data.charAt(i)) == null){
                freq.put(data.charAt(i), 1);
            }
            else{
                freq.put(data.charAt(i), freq.get(data.charAt(i)) + 1);
            }
        }

        PriorityQueue<StandardHuffmanNode> tree = new PriorityQueue<>(freq.size(),new HuffmanNodeComparator());

        for (Character c: freq.keySet()) {
            StandardHuffmanNode standardHuffmanNode = new StandardHuffmanNode();
            standardHuffmanNode.frequency = freq.get(c);
            standardHuffmanNode.letter = c;
            standardHuffmanNode.left = null;
            standardHuffmanNode.right = null;
            tree.add(standardHuffmanNode);
        }

        root = tree.peek();
        while (tree.size() > 1){
            StandardHuffmanNode l = tree.peek();
            tree.poll();

            StandardHuffmanNode r = tree.peek();
            tree.poll();

            StandardHuffmanNode parent = new StandardHuffmanNode();
            parent.left = l;
            parent.right = r;
            parent.frequency = l.frequency + r.frequency;
            parent.letter = '-';

            root = parent;
            tree.add(parent);
        }
    }

    private void createCodeTable(StandardHuffmanNode node, String code){
        if(node == null) {
            return;
        }
        if(node.left == null && node.right == null && Character.isLetter(node.letter)){
            if(Objects.equals(code, "")){
                codeTable.put(node.letter, "0");
                return;
            }
            codeTable.put(node.letter, code);
            return;
        }

        createCodeTable(node.left, code + "0");
        createCodeTable(node.right, code + "1");
    }

    public Map<Character, String> getCodeTable(){
        buildStandardHuffmanTree();
        createCodeTable(root, "");
        return codeTable;
    }
}

class HuffmanNodeComparator implements Comparator<StandardHuffmanNode> {
    @Override
    public int compare(StandardHuffmanNode x, StandardHuffmanNode y) {
        return x.frequency - y.frequency;
    }
}