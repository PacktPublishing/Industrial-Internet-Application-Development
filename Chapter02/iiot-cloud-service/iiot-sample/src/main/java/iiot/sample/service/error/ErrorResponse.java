package iiot.sample.service.error;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ErrorResponse {
    private String path;
    private String errorId;
    private String errorMessage;
    private String timestamp;

    public ErrorResponse(String errorId, String errorMessage, String path) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

        this.errorId = errorId;
        this.errorMessage = errorMessage;
        this.path = path;
        this.timestamp = dateFormat.format(new Date());
    }
}
