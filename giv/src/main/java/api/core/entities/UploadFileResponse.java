package api.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents upload response message that the end-user sees after uploading a file.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-09-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileResponse {
  private String fileName;
  private String fileDownloadUri;
  private String fileType;
  private long size;
}
