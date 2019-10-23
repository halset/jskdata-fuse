package no.jskdata.fuse;

import java.io.IOException;

import jnr.ffi.Pointer;
import no.jskdata.data.URLFileInfo;
import ru.serce.jnrfuse.struct.FileStat;

class FileNode extends Node {

    private URLFileInfo file;

    FileNode(JSKDataFS fs, URLFileInfo file) {
        super(fs);
        this.file = file;
    }

    @Override
    protected void getattr(FileStat stat) {
        stat.st_mode.set(FileStat.S_IFREG | 0444);
        stat.st_size.set(file.getContentLength());
        stat.st_uid.set(fs.getContext().uid.get());
        stat.st_gid.set(fs.getContext().gid.get());
        stat.st_mtim.tv_sec.set(file.getLastModified().getTime()/1000);
        stat.st_mtim.tv_nsec.set((file.getLastModified().getTime()%1000)*1000);
    }

    int read(Pointer buffer, long size, long offset) {
        int bytesToRead = (int) Math.min(file.getContentLength().longValue() - offset, size);
        
        try {
            byte[] bytesRead = file.read(offset, bytesToRead);
            buffer.put(0, bytesRead, 0, bytesToRead);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bytesToRead;
    }

    @Override
    protected String getName() {
        return file.getFileName();
    }

}