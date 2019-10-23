package no.jskdata.fuse;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import no.jskdata.data.URLFileInfo;

class DatasetDirectoryNode extends DirectoryNode {
    
    private final String datasetId;
    
    DatasetDirectoryNode(JSKDataFS fs, String datasetId) {
        super(fs, datasetId);
        this.datasetId = datasetId;
    }
    
    private Map<String, URLFileInfo> files;
    Map<String, URLFileInfo> getFiles() throws IOException {
        if (files == null) {
            files = new HashMap<>();
            for (URLFileInfo file :  fs.getDownloader().filesForDataset(datasetId)) {
                files.put(file.getFileName(), file);
            }
        }
        return files;
    }

    @Override
    public Collection<String> childNames() {
        try {
            return getFiles().keySet();
        } catch (IOException | RuntimeException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Node createChildForName(String name) {
        try {
            URLFileInfo file = getFiles().get(name);
            return new FileNode(fs, file);
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

}
