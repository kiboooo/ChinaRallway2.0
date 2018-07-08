package com.atguigu.chinarallway.Bean;

/**
 * Created by Kiboooo on 2017/10/6.
 */

public class ModifyData {
    String name;
    String data;

    public ModifyData(){}

    public ModifyData(String Name, String Data) {
        name = Name;
        data = Data;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(String data) {
        this.data = data;
    }

}
