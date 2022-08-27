package com.newland.trait.java8;

/**
 * 构造器引用
 */
public class Test1 {
    public static void main(String[] args) {
        Article article=new Article();
        article.setData("234234234".getBytes());
        //例如下面构造器引用，相当于
//		article.setCallback(new Callback() {
//			@Override
//			public String getContent(byte[] buffer) {
//				return new String(buffer);
//			}
//		});
        article.setCallback(String::new);
        System.out.println(article.getText());
    }
    interface Callback{
        public String getContent(byte[] buffer);
    }
    public static class Article{
        private Callback callback;
        private byte[] data;
        public void setCallback(Callback callback) {
            this.callback = callback;
        }
        public void setData(byte[] data) {
            this.data = data;
        }
        public String getText() {
            return this.callback.getContent(this.data);
        }
    }
}
