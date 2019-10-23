package no.jskdata.fuse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jnr.ffi.Pointer;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.struct.FileStat;

abstract class DirectoryNode extends Node {

    private String directoryName;
    private Map<String, Node> nodeByName = new HashMap<>();

    DirectoryNode(JSKDataFS fs, String name) {
        super(fs);
        this.directoryName = name;
    }

    public abstract Collection<String> childNames();

    public abstract Node createChildForName(String name);
    
    Node child(String name) {
        Node node = nodeByName.get(name);
        if (node == null) {
            node = createChildForName(name);
            if (node != null) {
                nodeByName.put(name, node);
            }
        }
        return node;
    }

    @Override
    protected Node find(String path) {
        if (super.find(path) != null) {
            return super.find(path);
        }
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (!path.contains("/")) {
            return child(path);
        }
        String nextName = path.substring(0, path.indexOf("/"));
        String rest = path.substring(path.indexOf("/"));
        return child(nextName).find(rest);
    }

    @Override
    protected void getattr(FileStat stat) {
        stat.st_mode.set(FileStat.S_IFDIR | 0555);
        stat.st_uid.set(fs.getContext().uid.get());
        stat.st_gid.set(fs.getContext().gid.get());
    }

    @Override
    protected String getName() {
        return directoryName;
    }

    public synchronized void read(Pointer buf, FuseFillDir filler) {
        for (String childName : childNames()) {
            Node node = child(childName);
            filler.apply(buf, node.getName(), null, 0);
        }
    }

}