package com.krupet.entity;

import lombok.Value;

/**
 * The class ResultData. Represent parsing result for particular string.
 */
@Value
public class ResultData {

    /** String value */
    private final String value;

    /** Number of occurrences of particular string in all files that were parsed */
    private final Long count;
}
