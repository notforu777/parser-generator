package lab4gen.Calc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tree {
    public String node;
    public List<Tree> children;

    public int v = 0;

    public Tree(String node, Tree... children) {
        this.node = node;
        this.children = Arrays.asList(children);
    }

    public Tree(String node) {
        this.node = node;
        this.children = new ArrayList<>();
    }

    public int getV() {
        return v;
    }

    public void setV(int ans) {
        this.v = ans;
    }
}
