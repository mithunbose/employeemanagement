package com.rakuten.test.employee.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mithbose
 */
@Getter
@Setter
public class FileUploadResponse {

    int invalidRowCount;
    int validRowCount;
    boolean isDbPersistanceSuccess;
}
