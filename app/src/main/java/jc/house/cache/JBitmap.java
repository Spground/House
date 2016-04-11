package jc.house.cache;

/**
 * Created by hzj on 2016/3/30.
 */
public class JBitmap {

    private boolean canRecycle;
    private int size;
    private int hitCount;
    private boolean needSave;
    public JBitmap() {
        canRecycle = true;
        needSave = true;
    }
}
