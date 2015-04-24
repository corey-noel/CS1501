/**
 * Created by Corey on 1/28/2015.
 */
public class DLBDictionary implements DictionaryInterface {

    private DLBDictionary sibling;
    private DLBDictionary child;
    private Character value;

    private DLBDictionary(Character v) {
        sibling = null;
        child = null;
        value = v;
    }

    public DLBDictionary() {
        sibling = null;
        child = null;
        value = null;
    }

    public boolean hasSibling() { return sibling != null; }
    public boolean hasChild() { return child != null; }

    /**
     * @param val the value stored in the child node
     * @return a direct child node containing the val, creating one if necessary
     */
    public DLBDictionary getChild(char val) {
        if (!hasChild()) {
            child = new DLBDictionary(val);
            return child;
        }

        return child.getSibling(val);
    }

    /**
     * @param val the  value stored in the sibling node
     * @return a sibling node containing the val, creating one if necessary
     */
    public DLBDictionary getSibling(char val) {
        if (value == val)
            return this;

        if (hasSibling())
            return sibling.getSibling(val);

        DLBDictionary node = new DLBDictionary(val);

        node.sibling = sibling;
        sibling = node;

        return node;
    }

    /**
     * @param val the value stored in the child node
     * @return a child node containing the val, or null if that child does not exist
     */
    public DLBDictionary searchChild(char val) {
        if (!hasChild()) {
            return null;
        }

        return child.searchSibling(val);
    }

    /**
     * @param val the value stored in the sibling node
     * @return a sibling node containing the val, or null if that sibling does not exist
     */
    public DLBDictionary searchSibling(char val) {
        if (value == val)
            return this;

        if (hasSibling())
            return sibling.getSibling(val);

        return null;
    }

    public boolean add(String s) {
        if (s.length() == 0) {
            getChild('\0');
            return true;
        }

        return getChild(s.charAt(0)).add(s.substring(1));
    }

    public int search(StringBuilder s) {
        if (s.length() == 0) {
            int result = 0;

            //is word
            if (searchChild('\0') != null)
                result += 2;

            //is prefix
            if ((child != null) && ((child.value != '\0') || child.hasSibling()))
                result += 1;

            return result;
        }

        DLBDictionary node = searchChild(s.charAt(0));
        if (node == null)
            return 0;

        s.deleteCharAt(0);
        return node.search(s);
    }

    public int getAll() {

    }
}
