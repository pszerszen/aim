package aim.hanoi;

import java.util.Stack;
import java.util.StringJoiner;

public class Tower extends Stack<Integer> {
    private static final long serialVersionUID = -6294097724154676304L;

    public boolean canPush(Integer integer) {
        return isEmpty() || peek() > integer;
    }

    @Override
    public synchronized boolean equals(Object o) {
        Tower other = (Tower) o;
        if (size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (elementAt(i) != other.elementAt(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public synchronized Tower clone() {
        return (Tower) super.clone();
    }

    @Override
    public synchronized String toString(){
        StringJoiner stringJoiner = new StringJoiner(", ", "Tower=[", "]");
        for(int i=0;i<size();i++){
            stringJoiner.add(Integer.toString(elementAt(i)));
        }
        return stringJoiner.toString();
    }

    @Override
    public Integer push(Integer e) {
        if (canPush(e)) {
            return super.push(e);
        }
        return null;
    }
}
