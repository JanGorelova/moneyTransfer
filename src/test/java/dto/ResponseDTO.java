package dto;

public class ResponseDTO {
    private String entity;

    private int status;

    public ResponseDTO(String entity, int status) {
        this.entity = entity;
        this.status = status;
    }

    public String getEntity() {
        return entity;
    }

    public int getStatus() {
        return status;
    }
}
