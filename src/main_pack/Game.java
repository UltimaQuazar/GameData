package main_pack;

import combat_usage.Combat_Game;
import graphic_launch.*; //graphics package for image loading
import input.KeyManager;
import input.MouseManager;
import states.*;

import java.awt.*;
import java.awt.image.*;

/**
 * Created by Parzival on 4/19/2016.
 * going to be the main code for the game, where everything gets made
 */
public class Game implements Runnable{

    //public vars
    public int width, height;
    public String title;
    public Display dis;
    public Graphics g;
    public State gState;
    public State invS;
    public State menuS;
    public State cState;
    public Combat_Game combat;
    public int fps;

    //private Vars
    private boolean running;
    private Thread thread;
    private BufferStrategy buff; //used to prevent screen flashing
    private KeyManager keyM;
    private MouseManager mouseM;
    private Game_Camera cam;
    private Handler hands;

    public Game(String t, int w, int h){
        this.width=w;
        this.height=h;
        this.title=t;
        keyM=new KeyManager();
        mouseM=new MouseManager();
        //combat=new Combat_Game(hands);
    }

    public void init(){
        dis=new Display(title,width,height);
        dis.getFrame().addKeyListener(keyM);
        dis.getFrame().addMouseListener(mouseM);
        dis.getFrame().addMouseMotionListener(mouseM);
        dis.getCanvas().addMouseListener(mouseM);
        dis.getCanvas().addMouseMotionListener(mouseM);
        Assets.init();

        hands=new Handler(this);
        cam = new Game_Camera(hands, 0,0);
        gState=new GameState(hands, GameState.getRoom(1));
        invS = new InvState(hands);
        menuS=new MenuState(hands);
        cState=new CombatState(hands);
        StateManager.setState(menuS);
    }

    /**
     * used to draw things to the canvas
     */
    private void render(){
        buff=dis.getCanvas().getBufferStrategy();
        if(buff == null){ //nothing in the canvas
            dis.getCanvas().createBufferStrategy(3);
            return;
        }
        g=buff.getDrawGraphics();
        g.clearRect(0,0,width,height); //used to clean whatever is on it

       //g.drawImage(map, 0, 0, null);
        if(StateManager.getState()!=null)
            StateManager.getState().render(g);
        //g.drawImage(Assets.player, 0, 10, null);
        //below use to show the drawn obj and clean the waste
        buff.show();
        g.dispose();
    }

    private void tick(){
        //x+=1;
        keyM.tick();
        if(StateManager.getState()!=null)
            StateManager.getState().tick();
    }

    public void run(){
        init();
        fps=60;
        double tpt=1000000000/fps; //100000000 nanosecs per 1 sec
        double delta=0;
        long now;
        long last=System.nanoTime();

        while(running){
            now = System.nanoTime();
            delta+=(now-last)/tpt;
            last = now;
            if(delta>=1) {
                tick();
                render();
                delta--;
            }
        }
        stop();
    }

    //getters
    public KeyManager getKeyManager(){
        return keyM;
    }
    public MouseManager getMouseM(){
        return mouseM;
    }
    public Game_Camera getCam(){
        return cam;
    }

    /**
     * This is to sync up the thread and the states
     * to try and set up the running variable
     */
    public synchronized void start(){
        if(running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * This is to sync up the thread and the states
     * to try and turn off the running variable
     */
    public synchronized void stop(){
        if (!running)
            return;
        running = false;
        //error trapped to get the reason why it didnt work
        try{
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}
