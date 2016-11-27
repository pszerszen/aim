package aim.hanoi;

import java.util.Objects;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.stream.IntStream;

public class Tower extends Stack<Integer> {
    private static final long serialVersionUID = -6294097724154676304L;

    boolean canPush(Integer integer) {
        return isEmpty() || peek() > integer;
    }

    @Override
    public Integer push(Integer e) {
        if (canPush(e)) {
            return super.push(e);
        }
        return null;
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (o instanceof Tower) {
            Tower other = (Tower) o;
            return equals(other);
        }
        return false;
    }

    private boolean equals(Tower other) {
        return size() == other.size() &&
                IntStream.range(0, size()).boxed()
                        .allMatch(i -> Objects.equals(elementAt(i), other.elementAt(i)));
    }

    @Override
    public synchronized int hashCode() {
        StringJoiner stringJoiner = new StringJoiner("-");
        for (int i = 0; i < size(); i++) {
            stringJoiner.add(Integer.toString(elementAt(i)));
        }
        return stringJoiner.toString().hashCode();
    }

    @Override
    public synchronized Tower clone() {
        return (Tower) super.clone();
    }

    @Override
    public synchronized String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        for (int i = 0; i < size(); i++) {
            stringJoiner.add(Integer.toString(elementAt(i)));
        }
        return stringJoiner.toString();
    }
}
