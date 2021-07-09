package lab8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class Window extends JFrame implements MouseListener, KeyListener {

    Statistics statistics;
    JLabel[] myLabels;
    Grid grid;
    boolean update = true;
    Animal tracked_animal;
    public Window(Map<String,Integer> parameters){
        super("Grid");
        statistics = new Statistics(this,parameters.get("statsOn"));
        this.addKeyListener(this);
        this.grid = new Grid(parameters);
        setSize(900,900);

        setLayout(new GridLayout(grid.getHeight(), grid.getWidth()));
        myLabels = new JLabel[grid.getWidth()* grid.getHeight()];

        for (int x = 0;x < myLabels.length;x++){

            myLabels[x] = new JLabel("");
            myLabels[x].setOpaque(true);
            myLabels[x].setBackground(Color.WHITE);
            myLabels[x].setBorder(BorderFactory.createLineBorder(Color.black));
            myLabels[x].addMouseListener(this);
        }
        for (JLabel myLabel : myLabels) add(myLabel);
        this.setVisible(true);
        draw();
    }
    public void run()
    {
        if(update) {
            grid.cycle();
            System.out.println("Map id " + name() + " " + statistics);
            draw();
        }
    }
    public int name()
    {
        return System.identityHashCode(this);
    }
    private void draw()//Color GUI
    {
        clear();
        for (Position position: grid.AnimalsOnMap.keySet()) {
            myLabels[position.y * grid.getWidth() + position.x].setBackground(grid.AnimalsOnMap.get(position).color);
        }
        for (Position position: grid.PlantsOnMap.keySet()) {
            myLabels[position.y * grid.getWidth() + position.x].setBackground(Color.GREEN);
        }
        if(tracked_animal!=null && tracked_animal.getEnergy()>0)
            myLabels[tracked_animal.position.y * grid.getWidth() + tracked_animal.position.x].setBackground(Color.MAGENTA);
    }
    private void clear()//Clear GUI
    {
        for (JLabel Label:myLabels) {
            Label.setBackground(Color.lightGray);
        }
        //Jungle
        for(int x = Math.floorDiv(grid.getWidth() - grid.getJungleRatio(),2); x < Math.floorDiv(grid.getWidth() - grid.getJungleRatio(),2) + grid.getJungleRatio(); x++)
        {
            for(int y = Math.floorDiv(grid.getHeight() - grid.getJungleRatio(),2); y < Math.floorDiv(grid.getHeight() - grid.getJungleRatio(),2) + grid.getJungleRatio(); y++)
                myLabels[y * grid.getWidth() + x].setBackground(Color.CYAN);
        }
    }
    private void colorBestGenom()//Color best genome
    {
        Genes genes=statistics.Most_common_genes(grid.GenesRightNow);
        for(Animal animal: grid.animals){
            if (animal.getGenes().equals(genes)){
                myLabels[animal.position.y * grid.getWidth() + animal.position.x].setBackground(Color.BLACK);
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent e) { }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) update = !update;
        else if (e.getKeyCode() == KeyEvent.VK_R) colorBestGenom();
    }
    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel clickedPanel = (JLabel) e.getSource();
        int index = 0;
        while (clickedPanel != myLabels[index])
            index++;
        System.out.println(new Position(index % grid.getWidth(),index / grid.getWidth()));
        if(!grid.AnimalsOnMap.containsKey(new Position(index % grid.getWidth(),index / grid.getWidth())))
            return;
        Animal tmp = grid.AnimalsOnMap.get(new Position(index % grid.getWidth(),index / grid.getWidth())).strongest();
        if(tmp == tracked_animal) {
            clickedPanel.setBackground( grid.AnimalsOnMap.get(new Position(index % grid.getWidth(), index / grid.getWidth())).color);
            tracked_animal = null;
        }
        else {
            if(tracked_animal != null && tracked_animal.getEnergy() > 0)
                myLabels[tracked_animal.position.x + tracked_animal.position.y * grid.getWidth()].setBackground(grid.AnimalsOnMap.get(tracked_animal.position).color);
            tracked_animal = tmp;
            clickedPanel.setBackground(Color.MAGENTA);
            System.out.println(tracked_animal);
        }
    }
    public void mousePressed(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}

}
