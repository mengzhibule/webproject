public class Test {
    public static void main(String[] args) {
        String url = "com.ss.action.IndexAction.action";
        String action = url.substring(0,url.lastIndexOf(".action"));
        System.out.println(action);
    }
}
