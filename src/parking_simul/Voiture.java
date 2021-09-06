package parking_simul;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;


public class Voiture extends JLabel implements Runnable{

    private final int dx=2; //px
    private final int dy=0; //px
    private final int dt=15; //ms
    private Parking parking;
    private final String color = getColor();
    ImageIcon v1= new ImageIcon(main_parking.imgDir+color+"1.png");  //position horizontale
    ImageIcon v2 = new ImageIcon(main_parking.imgDir+color+"4.png"); //position verticale

    public Voiture(Parking p) {
        super();
        this.parking=p;
        this.setIcon(v1);
        this.setBounds(-114 , 150, 114, 114);
    }
    private String getColor(){
        int i = Math.round((float) Math.random());  // 0 ou 1
        if(i==0){
            return "voiture1"; // coulour bleu
        }
        return "voiture2"; //couleur rouge
    }
    private void GetIn(){
        int x=this.getX();
        int y=this.getY();
        
        while (x<9) {            
            x=x+dx;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        this.setIcon(v1);
        while (x<100) {            
            x=x+dx;
            y=y+dy;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
    private void park(int i){
        int x=this.getX();
        int y=this.getY();
        int[] pos= {230, 300, 380, 455, 540}; //pos par raport a x
        while (x>170) {            
            x=x+dx;
            y=y+dy;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        this.setIcon(v1);        
        while (x<pos[i]) {            
            x=x+dx;         

            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }

        this.setIcon(v2);
        while (y>70) {            
            y=y-dx;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
    
// sortir 
    private void WayOutA(){
        int x=this.getX();
        int y=this.getY();
        //retourner
        while (y<150) { 
            y=y+3;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        //leaving until the door
        this.setIcon(v1);
        while (x<700) { 
            x=x+dx;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
    
// get out the park    
    private void WayOutB(){
        int x=this.getX();
        int y=this.getY();
        //leaving the park
        this.setIcon(v1);
        while (x<850) {            
            x=x+3;
            y=y-dy;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }

    }

    
    @Override
    public void run() {
    	
        try {
            parking.Ent.acquire();
            GetIn();
            parking.park.acquire();
            parking.coul.acquire();
            parking.Ent.release();
            int p = parking.getFree();
            parking.SetAside(p);
            this.park(p);
            parking.coul.release();
            Thread.sleep(Math.round((float) (Math.random()*5000)+5000)); //interuption d'un temps aleatoire entre 5 et 10 sec
            parking.coul.acquire();
            parking.park.release();
            parking.SetFree(p);
            this.WayOutA();
            parking.coul.release();
            parking.Sort.acquire();
            this.WayOutB();
            parking.Sort.release();
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
