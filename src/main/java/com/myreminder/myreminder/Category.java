package com.myreminder.myreminder;
import java.util.*;
public class Category {

        public String CategoryName ;

        public Category(String name)
        {
            this.CategoryName = name;

        }

        public String getCategoryName()
        {
            return CategoryName;
        }

        public void setCategoryName(String name) {
            this.CategoryName = name;
        }

}
