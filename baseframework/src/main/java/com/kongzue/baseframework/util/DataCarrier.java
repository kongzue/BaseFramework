package com.kongzue.baseframework.util;

/**
 * 解决 Java 无法返回多个数据的问题
 * return 时可以使用 DataCarrier 来完成：return newCarrier("text", true, 3);
 * 获取对应索引的值：result.value(0) 或 if (result.value(1)){ ... }
 * 参与计算：result.intValue(2) + 5
 */
public class DataCarrier {
    Object[] data;

    public DataCarrier(Object... data) {
        this.data = data;
    }

    public static DataCarrier newCarrier(Object... data) {
        return new DataCarrier(data);
    }

    public <T> T value(int index) {
        return (T) data[index];
    }

    public int intValue(int index) {
        return (int) value(index);
    }

    public String stringValue(int index) {
        return (String) value(index);
    }

    public boolean booleanValue(int index) {
        return (boolean) value(index);
    }

    public double doubleValue(int index) {
        return (double) value(index);
    }

    public short shortValue(int index) {
        return (short) value(index);
    }

    public float floatValue(int index) {
        return (float) value(index);
    }

    //eg.举例
//    public DataCarrier cc() {                 //有一个方法 cc() 需要同时返回多个数据
//        return newCarrier("text", true, 3);   //使用 DataCarrier 承载多个数据
//    }
//
//    public void dd(){
//        DataCarrier result = cc();
//        String a = result.value(0);           //获取第一个数据文本
//        boolean a2 = result.value(1);         //获取第二个布尔数据
//        if (result.value(1)){                 //直接参与判断
//            //do something...
//        }
//        int d = result.intValue(2) + 5;       //参与计算可以获取对应类型的值
//    }
}
