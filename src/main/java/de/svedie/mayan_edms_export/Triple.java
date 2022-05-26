package de.svedie.mayan_edms_export;

/**
 * Holds the information about the files.
 */
public class Triple<F, I, N> {

    private final String file;
    private final Integer id;
    private final String filename;

    public Triple(String file, Integer id, String filename) {
        this.file = file;
        this.id = id;
        this.filename = filename;
    }

    public String getFile() {
        return file;
    }

    public Integer getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }
}
