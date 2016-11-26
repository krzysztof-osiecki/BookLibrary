
package cieslik.karolina.booklibrary.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("volumeInfo")
    @Expose
    private VolumeInfo volumeInfo;

    /**
     * 
     * @return
     *     The volumeInfo
     */
    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    /**
     * 
     * @param volumeInfo
     *     The volumeInfo
     */
    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

}
