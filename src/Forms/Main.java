package Forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    private JPanel panelMain;
    private JPanel panelTitle;
    private JPanel panelCenter;
    private JLabel labelTime;
    private JLabel labelPuntos;
    private JLabel labelVidas;
    private int seconds = 0;
    private int vidas = 3;
    private int puntos = 0;
    private JLabel labelTrash;
    private JLabel labelPaper;
    private JButton buttonPause;

    public Main() {
        panelMain.setPreferredSize(new Dimension(800, 600));
        panelMain.setSize(new Dimension(800, 600));
        panelMain.setLayout(null);

        showPanelTitle();
        showPanelCenter();

        Timer timer = new Timer(1000, new TimerActionListener());
        timer.start();

        buttonPause.addMouseListener(new buttonMouseListener(timer));
        panelMain.addKeyListener(new PanelMainListener());
        panelMain.setFocusable(true);

        Timer paperTimer = new Timer(40, new PaperTimer());
        paperTimer.start();


    }

    /**
     * Se modifica el número de puntos del jugador teniendo en cuenta que solo se suman 10 puntos si el papel toca la papelera.
     * Luego, desaparece el papel y vuelve a aparecer en la parte superior de la pantalla en una posición 'x' aleatoria.
     */
    private void setLabelPuntos() {
        int random = (int) (Math.random() * (panelCenter.getWidth()));

        if ((labelPaper.getX() + labelPaper.getWidth()) >= labelTrash.getX() && labelPaper.getX() <= (labelTrash.getX() + labelTrash.getWidth()) && labelTrash.getY() <= labelPaper.getY()) {
            puntos += 10;
            labelPuntos.setText("Puntos: " + puntos);
            labelPaper.setLocation(random, 0);
        }
    }

    /**
     * Se modifica en número de vidas del jugador teniendo en cuenta que se pierde 1 vida cada vez que el papel toca la parte inferior de la pantalla.
     * Luego, desaparece el papel y vuelve a aparecer en la parte superior de la pantalla en una posición 'x' aleatoria.
     */
    private void setLabelVidas() {
        int random = (int) (Math.random() * (panelCenter.getWidth()));

        if (labelPaper.getY() == panelCenter.getHeight() - labelPaper.getHeight()) {
            vidas -= 1;
            labelVidas.setText("Vidas: " + vidas);
            labelPaper.setLocation(random, 0);
        }
    }

    /**
     *A partir de un Timer para el papel, en el que sindica que cada 40 milisegundos se mueve y+5 en el mismo eje x.
     */
    private class PaperTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

                if (labelPaper.getY() <= panelCenter.getHeight() - labelPaper.getHeight() && ((labelPaper.getX() + labelPaper.getWidth()) <= labelTrash.getX() || labelPaper.getX() >= (labelTrash.getX() + labelTrash.getWidth()) || labelTrash.getY() >= labelPaper.getY()) && buttonPause.getText().equals("Pausar") && vidas > 0 && puntos < 50) {
                    int y = labelPaper.getY();
                    labelPaper.setLocation(labelPaper.getX(), y + 5);
                    setLabelVidas();
                    setLabelPuntos();
                }
        }
    }

    /**
     * Se añade un KeyListener a la papelera, donde se moverá x+5 o x-5 sin salirse de la pantalla.
     */
    private class PanelMainListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int x = labelTrash.getX();

            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT -> x += 5;
                case KeyEvent.VK_LEFT -> x -= 5;
            }

            if (x >= 0 && x <= panelMain.getWidth() - labelTrash.getWidth()) {
                labelTrash.setLocation(x, labelTrash.getY());
            }
        }
    }

    /**
     * Pasamos el timer del juego mediante un constructor.
     * Cambiamos el texto y paramos o reanudamos el timer según el texto escrito en el botón.
     */
    private class buttonMouseListener extends MouseAdapter {
        Timer timer;

        public buttonMouseListener(Timer timer) {
            this.timer = timer;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //super.mouseClicked(e);
            if (buttonPause.getText().equals("Pausar")) {
                timer.stop();
                buttonPause.setText("Reanudar");
            } else {
                timer.restart();
                buttonPause.setText("Pausar");
            }
        }
    }

    /**
     * Por cada segundo que pase, cambiamos el texto escrito en el timer.
     */
    private class TimerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            seconds++;
            labelTime.setText(seconds + " segundos");
        }
    }

    /**
     * Panel superior del Frame donde colocamos: labelTime, buttonPause, labelPuntos y labelVidas, con sus valores y características iniciales.
     */
    private void showPanelTitle() {
        panelTitle = new JPanel();
        panelTitle.setLocation(0, 0);
        panelTitle.setSize(panelMain.getWidth(), 50);
        panelTitle.setBackground(Color.GRAY);
        panelMain.add(panelTitle);

        labelTime = new JLabel();
        labelTime.setText("0 segundos");
        panelTitle.add(labelTime);

        buttonPause = new JButton();
        buttonPause.setText("Pausar");
        buttonPause.setFocusPainted(false);
        buttonPause.setBackground(new Color(25, 18, 50));
        buttonPause.setForeground(new Color(255, 255, 255));
        panelTitle.add(buttonPause);

        labelPuntos = new JLabel();
        labelPuntos.setText("Puntos: 0");
        panelTitle.add(labelPuntos);

        labelVidas = new JLabel();
        labelVidas.setText("Vidas: 3");
        panelTitle.add(labelVidas);

    }

    /**
     * Panel central del Frame que incluye el papel y la papelera con sus valores y características iniciales.
     */
    private void showPanelCenter() {
        panelCenter = new JPanel();
        panelCenter.setLayout(null);
        panelCenter.setLocation(0, panelTitle.getHeight());
        panelCenter.setSize(panelMain.getWidth(), panelMain.getHeight() - panelTitle.getHeight());
        panelCenter.setBackground(Color.LIGHT_GRAY);
        panelMain.add(panelCenter);

        showTrash();
        showPaper();
    }

    /**
     * Método utilizado para mostrar la papelera con unas características iniciales.
     */
    private void showTrash() {
        labelTrash = new JLabel();
        labelTrash.setSize(90, 100);
        ImageIcon img = new ImageIcon("src/Images/trash.png");
        Icon icon = new ImageIcon(
                img.getImage().getScaledInstance(labelTrash.getWidth(), labelTrash.getHeight(), Image.SCALE_DEFAULT)
        );
        labelTrash.setIcon(icon);
        labelTrash.setLocation(panelCenter.getWidth() / 2 - labelTrash.getWidth() / 2, panelCenter.getHeight() - labelTrash.getHeight());
        panelCenter.add(labelTrash);
    }

    /**
     * Método utilizado para mostrar el papel con unas características iniciales.
     */
    private void showPaper() {
        int random = (int) (Math.random() * (panelCenter.getWidth()));

        labelPaper = new JLabel();
        labelPaper.setSize(30, 30);
        ImageIcon img = new ImageIcon("src/Images/paper.png");
        Icon icon = new ImageIcon(
                img.getImage().getScaledInstance(labelPaper.getWidth(), labelPaper.getHeight(), Image.SCALE_DEFAULT)
        );
        labelPaper.setIcon(icon);
        labelPaper.setLocation(random, 0);
        panelCenter.add(labelPaper);
    }

     public static void main(String[] args) {
        JFrame frame = new JFrame("panelMain");
        frame.setContentPane(new Main().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(350, 100);
        frame.setLayout(null);

        Toolkit pantalla = Toolkit.getDefaultToolkit();
        Image icono = pantalla.getImage("src/Images/politecnics.png");
        frame.setIconImage(icono);
    }
}
