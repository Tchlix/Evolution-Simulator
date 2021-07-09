package lab8;

public class Main {

    public static void main(String[] args) throws Exception {
        Parser parser = new Parser();
        int no_windows = parser.parameters.get("windows");
        Window[] windows = new Window[no_windows];
        for (int i = 0;i < no_windows; i++)
            windows[i] = new Window(parser.parameters);
        while (true) {
            for(Window window : windows)
                window.run();
            wait(100);//Time between cycles
        }
    }
    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}
