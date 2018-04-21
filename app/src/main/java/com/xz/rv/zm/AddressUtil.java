package com.xz.rv.zm;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 */

public class AddressUtil {



    /**
     * 读取assets的省市区文件
     *
     * @param context 上下文
     * @return List集合
     */
    public static List<CityEntity> getProvinceList(Context context) {
        List<CityEntity> cityEntities = null;
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(assetManager.open("province.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            stringBuilder = new StringBuilder();
            e.printStackTrace();
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           // assetManager.close();
        }
        if (!TextUtils.isEmpty(stringBuilder.toString())) {
            cityEntities = JsonMananger.jsonToList(stringBuilder.toString(), CityEntity.class);
        }

        return cityEntities;
    }


}
