package Compression.LZ77;

class LZ77Tag {
    public LZ77Tag(int position, int length, String next){
        this.offset = position;
        this.length = length;
        this.next = next;
    }

    @Override
    public String toString() {
        return "<"+ offset +","+length+","+next+">";
    }
    public int offset, length;
    public String next;
}
