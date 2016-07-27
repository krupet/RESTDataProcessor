package com.krupet.entity;

import lombok.Value;

/**
 * The class FileData. Wrapper entity that represent content of a file as byte array.
 */
@Value
public class FileData {

    /** File content */
    private final byte[] content;

}
