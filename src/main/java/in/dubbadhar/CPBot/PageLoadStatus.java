package in.dubbadhar.CPBot;

public class PageLoadStatus {
    private String err;
    private boolean success;

    public PageLoadStatus(boolean success)
    {
        this.success = success;
    }

    public PageLoadStatus(String err)
    {
        success = false;
        this.err = err;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public String getErr()
    {
        return err;
    }
}
