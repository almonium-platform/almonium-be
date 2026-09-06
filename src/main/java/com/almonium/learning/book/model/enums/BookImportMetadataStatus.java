package com.almonium.learning.book.model.enums;

/** Whether the bibliographic details of a private import still await the owner's confirmation. */
public enum BookImportMetadataStatus {
    /** Nothing detected yet: only what the owner typed at upload, if anything. */
    PENDING,
    /** The processor proposed values from the file header and the opening text. */
    PROPOSED,
    /** The owner confirmed or edited the details; later detections never overwrite them. */
    CONFIRMED
}
