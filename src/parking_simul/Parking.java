package parking_simul;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import javax.imageio.ImageIO;
import javax.swing.*;


public class Parking extends JFrame{

    private final JPanel panel = new ImagePanel(this);
    private boolean[] places={true, true, true, true, true};
    Semaphore Ent = new Semaphore(1);
    Semaphore Sort = new Semaphore(1);
    Semaphore coul = new Semaphore(1);
    Semaphore park = new Semaphore(5);
    
    public Parking() {
    	WinInitialisation();
    }
    private void WinInitialisation(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Simulation de Parking");
        this.setLocation(200, 120);
        this.setSize(846, 500);
        this.setResizable(false);
        this.setContentPane(panel);
        try {
            Image i = ImageIO.read(new File(main_parking.imgDir,"icon.png"));
            setIconImage(i);
        } catch (Exception e) {
            System.err.println(e);
        }
        this.setVisible(true);
    }
    public void begin(){
        Voiture v = new Voiture(this);
        Thread t = new Thread(v);
        this.panel.add(v);
        t.start();
    }
    public int getFree(){ 
        for(int i=0; i<5; i++){
            if (this.places[i]){
                return i; 
            }
        }
        return -1;
    }
    public void SetAside(int i){
        places[i]=false;
    }
    public void SetFree(int i){
        places[i]=true;
    }
}

class ImagePanel extends JPanel{
    private Image image;
    private Parking parking;
    private JTextField text ;
    private JButton button ;
    private JLabel message = new JLabel();
    public ImagePanel(Parking p) {
        super();
        parking=p;
        try {
            image = ImageIO.read(new File(main_parking.imgDir,"parking.png"));
        } catch (IOException e) {
            System.err.println(e);
        }
        this.setLayout(null);
        this.initComponents();
    }
    private void initComponents(){
        JLabel lab = new JLabel("Nombre de voitures :");
        lab.setBounds(630, 270, 120, 20);
        message.setBounds(630, 300, 200, 20);
        message.setForeground(Color.red);
        button = new JButton("Start");
        button.setBounds(670, 340, 100, 30);
        button.setBackground(new Color(35, 160, 7));
        text = new JTextField();
        text.setBounds(755, 270, 50, 25);
        text.setBackground(new Color(35, 160, 7));
        text.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        text.setFont(text.getFont().deriveFont(20f));
        text.setText("15");
        button.addActionListener((ActionEvent e) -> {
            message.setText("");
            int k=0;
            try {
                k = Integer.decode(text.getText());
            } catch (NumberFormatException ex) {
                message.setText("Entrer un numero entier!");
            }
            for(int i=0; i<k; i++){
                parking.begin();
            }
        });
        this.add(message);
        this.add(lab);
        this.add(button);
        this.add(text);
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}