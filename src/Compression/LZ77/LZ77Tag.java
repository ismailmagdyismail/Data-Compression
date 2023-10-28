package Compression.LZ77;

class LZ77Tag {
    public LZ77Tag(int position, int length, char next){
        this.position = position;
        this.length = length;
        this.next = next;
    }

    @Override
    public String toString() {
        return "<"+position+" , "+length+" , "+next+">";
    }
    public int position, length;
    public char next;
}
