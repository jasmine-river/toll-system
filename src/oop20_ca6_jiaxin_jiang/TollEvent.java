package oop20_ca6_jiaxin_jiang;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TollEvent
{

    private String reg;
    private long imageId;
    private String timestamp;

    public TollEvent(String reg, long imageId)
    {
        this.reg = reg;
        this.imageId = imageId;
        this.timestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();
    }

    public TollEvent(String reg, long imageId, String timestamp)
    {
        this.reg = reg;
        this.imageId = imageId;
        this.timestamp = timestamp;
    }

    public String getReg()
    {
        return reg;
    }

    public long getImageId()
    {
        return imageId;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    @Override
    public String toString()
    {
        return "TollEvent{" + "reg=" + reg + ", imageId=" + imageId + ", timestamp=" + timestamp + '}';
    }
}
