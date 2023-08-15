// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

       import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

        public class FlappyBird extends JFrame implements ActionListener, KeyListener {
            private final int WIDTH = 800;
            private final int HEIGHT = 600;
            private final int BIRD_SIZE = 30;
            private final int PIPE_WIDTH = 50;
            private final int PIPE_HEIGHT = 300;
            private final int PIPE_GAP = 200;
            private final int PIPE_SPEED = 5;
            private final int GRAVITY = 1;

            private int birdY;
            private int birdVelocity;
            private List<Rectangle> pipes;
            private boolean gameRunning;
            private int score;

            public FlappyBird() {
                setTitle("Flappy Bird");
                setSize(WIDTH, HEIGHT);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setResizable(false);
                setLocationRelativeTo(null);

                addKeyListener(this);

                birdY = HEIGHT / 2;
                birdVelocity = 0;
                pipes = new ArrayList<>();
                gameRunning = true;
                score = 0;

                Timer timer = new Timer(20, this);
                timer.start();

                generatePipe();
            }

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(Color.CYAN);
                g.fillRect(0, 0, WIDTH, HEIGHT);

                g.setColor(Color.ORANGE);
                g.fillRect(100, birdY, BIRD_SIZE, BIRD_SIZE);

                g.setColor(Color.GREEN);
                for (Rectangle pipe : pipes) {
                    g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
                }

                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.drawString("Score: " + score, 20, 30);

                if (!gameRunning) {
                    g.setFont(new Font("Arial", Font.PLAIN, 50));
                    g.drawString("Game Over", WIDTH / 2 - 120, HEIGHT / 2 - 50);
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    birdVelocity += GRAVITY;
                    birdY += birdVelocity;

                    if (birdY <= 0) {
                        birdY = 0;
                    }

                    for (Rectangle pipe : pipes) {
                        pipe.x -= PIPE_SPEED;

                        if (pipe.x + pipe.width <= 0) {
                            pipes.remove(pipe);
                            score++;
                            break;
                        }

                        if (pipe.intersects(new Rectangle(100, birdY, BIRD_SIZE, BIRD_SIZE)) ||
                                birdY + BIRD_SIZE >= HEIGHT) {
                            gameRunning = false;
                            break;
                        }
                    }

                    if (pipes.size() == 0 || WIDTH - pipes.get(pipes.size() - 1).x >= PIPE_GAP) {
                        generatePipe();
                    }

                    repaint();
                }
            }

            private void generatePipe() {
                Random random = new Random();
                int gapStart = random.nextInt(HEIGHT - PIPE_GAP);
                pipes.add(new Rectangle(WIDTH, 0, PIPE_WIDTH, gapStart));
                pipes.add(new Rectangle(WIDTH, gapStart + PIPE_GAP, PIPE_WIDTH, HEIGHT - gapStart - PIPE_GAP));
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && gameRunning) {
                    birdVelocity = -15;
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameRunning) {
                    restartGame();
                }
            }

            private void restartGame() {
                birdY = HEIGHT / 2;
                birdVelocity = 0;
                pipes.clear();
                gameRunning = true;
                score = 0;
                generatePipe();
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            public static void main(String[] args) {
                EventQueue.invokeLater(() -> {
                    FlappyBird flappyBird = new FlappyBird();
                    flappyBird.setVisible(true);
                });
            }
        }

