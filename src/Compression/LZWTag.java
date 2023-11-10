package Compression.LZW;

class LZWTag {
    public LZWTag(int index){
        this.index = index;
    }
    @Override
    public String toString() {
        return "<"+this.index+">";
    }
    int index;
}
