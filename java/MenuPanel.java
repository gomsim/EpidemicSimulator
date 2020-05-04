import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.*;

public class MenuPanel extends JPanel {

    private JButton startButton;
    private JButton stopButton;
    private JButton pauseButton;
    private JButton logButton;
    private JSlider tickRate;
    private JComboBox<Algorithm> algorithmsBox;
    private JCheckBox showCheckbox;
        
    private Visualizer visualizer;
    private static final int TICK_CEILING = 1000;
    
    public MenuPanel(Visualizer visualizer, Algorithm[] algorithms) {
        this.visualizer = visualizer;
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        ActionListener al = new ButtonListener();
        
        //Create Buttons
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        pauseButton = new JButton("Pause");
        logButton = new JButton("Log");
        tickRate = new JSlider(JSlider.HORIZONTAL, 1, TICK_CEILING, 500);
        algorithmsBox = new JComboBox<>(algorithms);
        showCheckbox = new JCheckBox("showSimulation",true);
        startButton.addActionListener(al);
        stopButton.addActionListener(al);
        pauseButton.addActionListener(al);
        logButton.addActionListener(al);
        tickRate.addChangeListener(new SliderListener());
        showCheckbox.addActionListener(al);
        add(startButton);
        add(stopButton);
        add(pauseButton);
        add(algorithmsBox);
        add(new JLabel("Tickrate: "));
        add(tickRate);
        add(logButton);
        add(showCheckbox);
    }

    

    public class SliderListener implements ChangeListener {
        public void stateChanged(ChangeEvent event){
            visualizer.setTickIntervall(TICK_CEILING - ((JSlider)event.getSource()).getValue());
        }
    }

    public class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if (event.getSource().equals(startButton)){
                visualizer.stop();
                System.out.println("Start");
                visualizer.start(((Algorithm)algorithmsBox.getSelectedItem()));
            }else if (event.getSource().equals(stopButton)){
                System.out.println("Stop");
                visualizer.stop();
                pauseButton.setText("Pause");
            }else if (event.getSource().equals(pauseButton)){
                if(visualizer.isPaused()){
                    pauseButton.setText("Pause");
                    visualizer.resume();
                }else{
                    pauseButton.setText("Resume");
                    visualizer.pause();
                }
            }else if (event.getSource().equals(algorithmsBox)){
                System.out.println("algorithmsBox");
            }else if (event.getSource().equals(logButton)){
                System.out.println("Log");
            }else if (event.getSource().equals(showCheckbox)){
                visualizer.showSimulation(!visualizer.isShowingSimulation());
            }
        }
    }
}