package cn.litterSortSystem.xiudian7.identify;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfo {
    private int imageId;
    private int userId;
    private String imagePath;
    private int garageType;//垃圾类别
}
