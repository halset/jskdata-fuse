package no.jskdata.fuse;

import java.util.Collection;

import jnr.ffi.Pointer;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import no.jskdata.Downloader;
import no.jskdata.GeoNorgeDownloadAPI;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseFileInfo;

/**
 * directory structure: organization / dataset name / file
 */
public class JSKDataFS extends FuseStubFS {

    private final GeoNorgeDownloadAPI downloader;
    private final DirectoryNode root;

    public JSKDataFS(GeoNorgeDownloadAPI downloader) {
        this.downloader = downloader;
        this.root = new DirectoryNode(this, "") {
            
            @Override
            public Collection<String> childNames() {
                return downloader.datasets();
            }

            @Override
            public Node createChildForName(String name) {
                return new DatasetDirectoryNode(JSKDataFS.this, name);
            }
        };
    }

    public GeoNorgeDownloadAPI getDownloader() {
        return downloader;
    }

    private Node nodeFromPath(String path) {
        return root.find(path);
    }

    @Override
    public int getattr(String path, FileStat stat) {
        Node p = nodeFromPath(path);
        if (p != null) {
            p.getattr(stat);
            return 0;
        }
        return -ErrorCodes.ENOENT();
    }

    @Override
    public int read(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {
        Node p = nodeFromPath(path);
        if (p == null) {
            return -ErrorCodes.ENOENT();
        }
        if (!(p instanceof FileNode)) {
            return -ErrorCodes.EISDIR();
        }
        return ((FileNode) p).read(buf, size, offset);
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filter, long offset, FuseFileInfo fi) {
        Node p = nodeFromPath(path);
        if (p == null) {
            return -ErrorCodes.ENOENT();
        }
        if (!(p instanceof DirectoryNode)) {
            return -ErrorCodes.ENOTDIR();
        }
        filter.apply(buf, ".", null, 0);
        filter.apply(buf, "..", null, 0);
        ((DirectoryNode) p).read(buf, filter);
        return 0;
    }

}
