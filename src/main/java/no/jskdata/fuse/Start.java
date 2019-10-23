package no.jskdata.fuse;

import java.nio.file.Paths;

import jnr.ffi.Platform;
import no.jskdata.GeoNorgeDownloadAPI;

public class Start {

    public static void main(String[] args) {
        
        GeoNorgeDownloadAPI downloader = new GeoNorgeDownloadAPI();
        downloader.dataset("28c896d0-8a0d-4209-bf31-4931033b1082"); // nrl
        downloader.dataset("aee42bb6-d0e9-4d70-86fe-6ea76c381055"); // n1000

        JSKDataFS fs = new JSKDataFS(downloader);
        try {
            String path;
            switch (Platform.getNativePlatform().getOS()) {
            case WINDOWS:
                path = "J:\\";
                break;
            default:
                path = "/tmp/mnt-jskdata";
            }
            fs.mount(Paths.get(path), true, true);
        } finally {
            fs.umount();
        }

    }

}
