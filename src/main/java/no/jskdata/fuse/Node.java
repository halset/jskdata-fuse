package no.jskdata.fuse;

import ru.serce.jnrfuse.struct.FileStat;

abstract class Node {

    protected final JSKDataFS fs;

    Node(JSKDataFS fs) {
        this.fs = fs;
    }

    protected abstract void getattr(FileStat stat);

    protected abstract String getName();

    protected Node find(String path) {
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.equals(getName()) || path.isEmpty()) {
            return this;
        }
        return null;
    }

}