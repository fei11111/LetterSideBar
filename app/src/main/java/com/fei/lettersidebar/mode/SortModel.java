package com.fei.lettersidebar.mode;

/**
 * @ClassName: SortModel
 * @Description: java类作用描述
 * @Author: Fei
 * @CreateDate: 2020-12-18 21:41
 * @UpdateUser: 更新者
 * @UpdateDate: 2020-12-18 21:41
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SortModel {

    private String name;//数据
    private String sortLetter;//首字母

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetter() {
        return sortLetter;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
    }
}
