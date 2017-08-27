package com.example.tmnt.uploadphoto;

import java.util.HashMap;

/**
 * Created by tmnt on 2016/8/19.
 */
public class UploadPhoto {

    public static String uploadPhoto(String fileName, String fileContent) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("FileName", fileName);
        params.put("FileContent", fileContent);

        String result = HttpUtil.RequestWebService("SaveFile", params);
        return result;
    }
}
