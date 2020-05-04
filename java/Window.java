import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.*;

class Window extends JFrame{

    private MenuPanel menuPanel;
    private Visualizer visualizer;
    
    public Window(Algorithm ... algorithms){
        super("SOC simulator");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    
        setLayout(new BorderLayout());
        
        visualizer = new Visualizer();
        menuPanel = new MenuPanel(visualizer, algorithms);

        add(menuPanel, BorderLayout.NORTH);
        add(visualizer, BorderLayout.CENTER);
        
        setVisible(true);
    }
    public static void setLookAndFeel(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (UnsupportedLookAndFeelException e) {
            System.out.println("UnsupportedLookAndFeelException");
        }catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException");
        }catch (InstantiationException e) {
            System.out.println("InstantiationException");
        }catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException");
        }
    }
}