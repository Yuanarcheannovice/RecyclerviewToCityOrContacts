package com.xz.rv.zm;

import java.io.Serializable;

/**
 * 省市区实体
 *
 * @author xz
 */
public class CityEntity implements Serializable {
    private int id;
    /**
     * 城市名
     */
    private String name;
    /**
     * 城市首字母
     */
    private String initials;
    /**
     * 上级ID
     */
    private int parentId;

    public CityEntity() {
    }

    public CityEntity(int id, String name, String initials, int parentId) {
        this.id = id;
        this.name = name;
        this.initials = initials;
        this.parentId = parentId;
    }

    public static CityEntity newInitials(String name,String initials){
       CityEntity cityEntity=new CityEntity();
       cityEntity.setId(-1);
       cityEntity.setInitials(initials);
       cityEntity.setName(initials);
       cityEntity.setParentId(-1);
       return cityEntity;
   }

    public static CityEntity newInitials(String initials){
        CityEntity cityEntity=new CityEntity();
        cityEntity.setId(-1);
        cityEntity.setInitials(initials);
        cityEntity.setName(initials);
        cityEntity.setParentId(-1);
        return cityEntity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }


    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
